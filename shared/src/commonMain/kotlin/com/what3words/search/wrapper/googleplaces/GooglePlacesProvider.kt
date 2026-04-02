package com.what3words.search.wrapper.googleplaces

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WError
import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.search.wrapper.core.ResolvableSearchProvider
import com.what3words.search.wrapper.core.SearchResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/** Unique identifier for the Google Places search provider. */
const val GOOGLE_PLACES_PROVIDER_ID = "GooglePlacesProvider"

private const val BASE_URL = "https://places.googleapis.com/v1/places"
private const val AUTOCOMPLETE_PATH = "$BASE_URL:autocomplete"
private const val HEADER_API_KEY = "X-Goog-Api-Key"
private const val HEADER_FIELD_MASK = "X-Goog-FieldMask"
private const val PLACE_DETAILS_FIELD_MASK = "id,displayName,formattedAddress,location"
private const val AUTOCOMPLETE_FIELD_MASK =
    "suggestions.placePrediction.placeId," +
            "suggestions.placePrediction.structuredFormat.mainText.text," +
            "suggestions.placePrediction.structuredFormat.secondaryText.text"
private const val EXTRAS_KEY_PLACE_ID = "placeId"
private const val QUERY_PARAM_SESSION_TOKEN = "sessionToken"

/**
 * [ResolvableSearchProvider] backed by the Google Places API (New).
 *
 * Searches via `places:autocomplete` and resolves suggestions to what3words addresses
 * by fetching place details and converting coordinates via [W3WTextDataSource.convertTo3wa].
 *
 * When [GooglePlacesConfig.useSessionTokens] is `true`, a UUID token groups each autocomplete
 * call with its paired place-details fetch into one billing session, rotating after every [resolve].
 *
 * @property config Provider configuration.
 * @property textDataSource Used for coordinate-to-what3words conversion.
 */
internal class GooglePlacesProvider internal constructor(
    private val config: GooglePlacesConfig,
    private val textDataSource: W3WTextDataSource,
    private val httpClient: HttpClient,
) : ResolvableSearchProvider {

    /** Public constructor used by [GooglePlacesSearch] and callers. */
    constructor(config: GooglePlacesConfig, textDataSource: W3WTextDataSource) : this(
        config = config,
        textDataSource = textDataSource,
        httpClient = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    )

    override val providerId: String = GOOGLE_PLACES_PROVIDER_ID

    @OptIn(ExperimentalUuidApi::class)
    private fun generateSessionToken() = Uuid.random().toString()

    /** Protects [currentSessionToken] for concurrent coroutine access. */
    private val tokenMutex = Mutex()

    /** Active session token; `null` when [GooglePlacesConfig.useSessionTokens] is `false`. */
    private var currentSessionToken: String? =
        if (config.useSessionTokens) generateSessionToken() else null

    /** Returns the current session token, or `null` if session tokens are disabled. */
    private suspend fun sessionToken(): String? =
        if (!config.useSessionTokens) null else tokenMutex.withLock { currentSessionToken }

    /** Generates a new session token. No-op when session tokens are disabled. */
    private suspend fun rotateSessionToken() {
        if (!config.useSessionTokens) return
        tokenMutex.withLock { currentSessionToken = generateSessionToken() }
    }

    /** Handles queries that meet or exceed [GooglePlacesConfig.minQueryLength]. */
    override fun canHandle(query: String): Boolean = query.length >= config.minQueryLength

    /** Applies the API key and any extra configured headers to the request. */
    private fun HttpRequestBuilder.applyCommonHeaders(fieldMask: String) {
        header(HEADER_API_KEY, config.apiKey)
        header(HEADER_FIELD_MASK, fieldMask)
        config.headers.forEach { (key, value) -> header(key, value) }
    }

    /**
     * POSTs to `places:autocomplete` and maps each prediction to a [SearchResult.SearchSuggestion].
     *
     * `extras["placeId"]` holds the place identifier required by [resolve].
     */
    override suspend fun executeSearch(query: String): W3WResult<List<SearchResult>> {
        return try {
            val token = sessionToken()
            val response = httpClient.post(AUTOCOMPLETE_PATH) {
                applyCommonHeaders(AUTOCOMPLETE_FIELD_MASK)
                contentType(ContentType.Application.Json)
                setBody(AutocompleteRequest(input = query, sessionToken = token))
            }

            if (!response.status.isSuccess()) {
                return W3WResult.Failure(response.toGooglePlacesApiError())
            }

            val results = response.body<AutocompleteResponse>().suggestions
                .take(config.maxResults)
                .mapNotNull { suggestion ->
                    val prediction = suggestion.placePrediction ?: return@mapNotNull null
                    SearchResult.SearchSuggestion(
                        query = query,
                        providerId = providerId,
                        title = prediction.structuredFormat?.mainText?.text
                            ?: prediction.text?.text.orEmpty(),
                        subtitle = prediction.structuredFormat?.secondaryText?.text
                            ?.takeIf { it.isNotEmpty() },
                        extras = mapOf(EXTRAS_KEY_PLACE_ID to prediction.placeId)
                    )
                }
            W3WResult.Success(results)
        } catch (e: Exception) {
            W3WResult.Failure(W3WError(e))
        }
    }

    /**
     * Fetches place details for the `placeId` in [data]'s extras and converts the coordinates
     * to a what3words address. The session token is sent to close the billing session opened by
     * [executeSearch] and is rotated afterwards regardless of success or failure.
     *
     * @param data Suggestion produced by [executeSearch].
     * @return [W3WResult.Success] with a [SearchResult.ResolvedAddress], or [W3WResult.Failure]
     *   if the place ID is missing, the network call fails, or coordinate conversion fails.
     */
    override suspend fun resolve(data: SearchResult.SearchSuggestion): W3WResult<SearchResult.ResolvedAddress> {
        val placeId = data.extras[EXTRAS_KEY_PLACE_ID]
            ?: return W3WResult.Failure(W3WError("Missing placeId in suggestion extras"))

        val token = sessionToken()

        return try {
            val response = httpClient.get("$BASE_URL/$placeId") {
                applyCommonHeaders(PLACE_DETAILS_FIELD_MASK)
                token?.let { parameter(QUERY_PARAM_SESSION_TOKEN, it) }
            }

            if (!response.status.isSuccess()) {
                return W3WResult.Failure(response.toGooglePlacesApiError())
            }

            val details = response.body<PlaceDetailsResponse>()
            val latLng = details.location
                ?: return W3WResult.Failure(W3WError("Place details missing location for placeId=$placeId"))

            val coordinates = W3WCoordinates(lat = latLng.latitude, lng = latLng.longitude)

            when (val w3wResult = textDataSource.convertTo3wa(coordinates, config.language)) {
                is W3WResult.Success -> W3WResult.Success(
                    SearchResult.ResolvedAddress(
                        query = data.query,
                        providerId = providerId,
                        address = w3wResult.value
                    )
                )
                is W3WResult.Failure -> W3WResult.Failure(w3wResult.error, w3wResult.message)
            }
        } catch (e: Exception) {
            W3WResult.Failure(W3WError(e))
        } finally {
            // Rotate the token after every place details fetch to start a fresh billing session.
            rotateSessionToken()
        }
    }
}
