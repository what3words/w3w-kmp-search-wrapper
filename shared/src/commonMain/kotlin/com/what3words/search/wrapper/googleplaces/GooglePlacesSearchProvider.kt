package com.what3words.search.wrapper.googleplaces

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WError
import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.search.wrapper.core.ResolvableSearchProvider
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_DISTANCE_TO_FOCUS
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_SUBTITLE
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_TITLE
import com.what3words.search.wrapper.core.SessionManager
import com.what3words.search.wrapper.core.safeW3WCall
import com.what3words.search.wrapper.error.MissingAddressIdException
import com.what3words.search.wrapper.googleplaces.model.AutocompleteRequest
import com.what3words.search.wrapper.googleplaces.model.AutocompleteResponse
import com.what3words.search.wrapper.googleplaces.model.CircleRequest
import com.what3words.search.wrapper.googleplaces.model.LatLng
import com.what3words.search.wrapper.googleplaces.model.LocationBiasRequest
import com.what3words.search.wrapper.googleplaces.model.PlaceDetailsResponse
import com.what3words.search.wrapper.googleplaces.model.RectangleRequest
import com.what3words.search.wrapper.googleplaces.model.toGooglePlacesApiError
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/** Unique identifier for the Google Places search provider. */
const val GOOGLE_PLACES_PROVIDER_ID = "GooglePlacesSearchProvider"

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
internal class GooglePlacesSearchProvider internal constructor(
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

    /** Active session manager; `null` when [GooglePlacesConfig.useSessionTokens] is `false`. */
    private val sessionManager: SessionManager? =
        if (config.useSessionTokens) SessionManager() else null

    /** Returns the current session token, or `null` if session tokens are disabled. */
    private fun sessionToken(): String? = sessionManager?.sessionToken

    /** Handles queries that meet or exceed [GooglePlacesConfig.minQueryLength]. */
    override fun canHandle(query: String): Boolean = query.length >= config.minQueryLength

    /** Headers sent with every autocomplete request. */
    private val autoCompleteHeaders: Map<String, String> by lazy {
        buildMap {
            put(HEADER_API_KEY, config.apiKey)
            put(HEADER_FIELD_MASK, AUTOCOMPLETE_FIELD_MASK)
            config.headers.forEach { (key, value) -> if (value != null) put(key, value) }
        }
    }

    /** Headers sent with every place-details request. */
    private val placeDetailHeaders: Map<String, String> by lazy {
        buildMap {
            put(HEADER_API_KEY, config.apiKey)
            put(HEADER_FIELD_MASK, PLACE_DETAILS_FIELD_MASK)
            config.headers.forEach { (key, value) -> if (value != null) put(key, value) }
        }
    }

    /** Applies a pre-built header map to the request. */
    private fun HttpRequestBuilder.applyHeaders(headers: Map<String, String>) {
        headers.forEach { (key, value) -> header(key, value) }
    }

    /**
     * POSTs to `places:autocomplete` and maps each prediction to a [SearchResult.SearchSuggestion].
     *
     * `extras["placeId"]` holds the place identifier required by [resolve].
     */
    override suspend fun executeSearch(query: String): W3WResult<List<SearchResult>> =
        withContext(Dispatchers.IO) {
            safeW3WCall {
                val token = sessionToken()

                val response = httpClient.post(AUTOCOMPLETE_PATH) {
                    applyHeaders(autoCompleteHeaders)
                    contentType(ContentType.Application.Json)
                    setBody(
                        AutocompleteRequest(
                            input = query,
                            sessionToken = token,
                            locationBias = config.locationBias?.toLocationBiasRequest(),
                            origin = config.origin?.let { LatLng(it.lat, it.lng) },
                            includedRegionCodes = config.includedRegionCodes.takeIf { it.isNotEmpty() },
                        )
                    )
                }

                if (!response.status.isSuccess()) {
                    return@safeW3WCall W3WResult.Failure(response.toGooglePlacesApiError())
                }

                val results = response.body<AutocompleteResponse>().suggestions
                    .take(config.maxResults)
                    .mapNotNull { suggestion ->
                        val prediction = suggestion.placePrediction ?: return@mapNotNull null
                        SearchResult.SearchSuggestion(
                            query = query,
                            providerId = providerId,
                            extras = buildMap {
                                put(EXTRAS_KEY_PLACE_ID, prediction.placeId)
                                put(
                                    EXTRAS_KEY_TITLE,
                                    prediction.structuredFormat?.mainText?.text
                                        ?: prediction.text?.text.orEmpty()
                                )
                                prediction.structuredFormat?.secondaryText?.text
                                    ?.takeIf { it.isNotEmpty() }
                                    ?.let { put(EXTRAS_KEY_SUBTITLE, it) }
                                prediction.distanceToOrigin?.let {
                                    put(
                                        EXTRAS_KEY_DISTANCE_TO_FOCUS,
                                        it.toString()
                                    )
                                }
                            }
                        )
                    }
                W3WResult.Success(results)
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
    override suspend fun resolve(data: SearchResult.SearchSuggestion): W3WResult<SearchResult.ResolvedAddress> =
        withContext(Dispatchers.IO) {
            try {
                safeW3WCall {
                    val placeId = data.extras[EXTRAS_KEY_PLACE_ID]
                        ?: return@safeW3WCall W3WResult.Failure(MissingAddressIdException())

                    val token = sessionToken()

                    val response = httpClient.get("$BASE_URL/$placeId") {
                        applyHeaders(placeDetailHeaders)
                        parameter(QUERY_PARAM_SESSION_TOKEN, token)
                    }

                    if (!response.status.isSuccess()) {
                        return@safeW3WCall W3WResult.Failure(response.toGooglePlacesApiError())
                    }

                    val details = response.body<PlaceDetailsResponse>()
                    val latLng = details.location
                        ?: return@safeW3WCall W3WResult.Failure(
                            W3WError("Place details missing location for placeId=$placeId")
                        )

                    val coordinates = W3WCoordinates(lat = latLng.latitude, lng = latLng.longitude)

                    when (val w3wResult =
                        textDataSource.convertTo3wa(coordinates, config.language)) {
                        is W3WResult.Success -> W3WResult.Success(
                            SearchResult.ResolvedAddress(
                                query = data.query,
                                providerId = providerId,
                                address = w3wResult.value
                            )
                        )

                        is W3WResult.Failure -> W3WResult.Failure(
                            w3wResult.error,
                            w3wResult.message
                        )
                    }
                }
            } finally {
                // Rotate the token after every place details fetch to start a fresh billing session.
                sessionManager?.refresh()
            }
        }
}

/** Converts a public [LocationBias] to the internal serializable [LocationBiasRequest]. */
private fun LocationBias.toLocationBiasRequest(): LocationBiasRequest = when (this) {
    is LocationBias.Circle -> LocationBiasRequest(
        circle = CircleRequest(
            center = LatLng(center.lat, center.lng),
            radius = radiusMeters,
        )
    )

    is LocationBias.Rectangle -> LocationBiasRequest(
        rectangle = RectangleRequest(
            low = LatLng(low.lat, low.lng),
            high = LatLng(high.lat, high.lng),
        )
    )
}
