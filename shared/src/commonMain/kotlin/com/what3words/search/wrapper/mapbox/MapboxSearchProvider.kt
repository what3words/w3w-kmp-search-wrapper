package com.what3words.search.wrapper.mapbox

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WError
import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.search.wrapper.core.ResolvableSearchProvider
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_DISTANCE_TO_FOCUS
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_SUBTITLE
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_TITLE
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_ZOOM_LEVEL
import com.what3words.search.wrapper.core.SessionManager
import com.what3words.search.wrapper.core.safeW3WCall
import com.what3words.search.wrapper.error.InvalidCoordinatesException
import com.what3words.search.wrapper.error.MissingAddressIdException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.appendPathSegments
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.concurrent.Volatile

/** Unique identifier for the Mapbox geocoding search provider. */
const val MAPBOX_PROVIDER_ID = "MapboxSearchProvider"

private const val BASE_URL = "https://api.mapbox.com/search/searchbox/v1"
private const val SUGGEST_PATH = "suggest"
private const val RETRIEVE_PATH = "retrieve"

private const val PARAM_QUERY = "q"
private const val PARAM_SESSION_TOKEN = "session_token"
private const val PARAM_ACCESS_TOKEN = "access_token"
private const val PARAM_LIMIT = "limit"
private const val PARAM_LANGUAGE = "language"
private const val PARAM_COUNTRY = "country"
private const val PARAM_PROXIMITY = "proximity"
private const val EXTRAS_KEY_MAPBOX_ID = "mapbox_id"

/**
 * [ResolvableSearchProvider] backed by the Mapbox Search Box API v1.
 *
 * Searches via `/search/searchbox/v1/suggest` and resolves suggestions to what3words addresses
 * by calling `/search/searchbox/v1/retrieve/{mapbox_id}` to fetch coordinates, then
 * converting them via [W3WTextDataSource.convertTo3wa].
 *
 * @property config Provider configuration including API key, language, and result limits.
 * @property textDataSource Used for coordinate-to-what3words conversion.
 */
internal class MapboxSearchProvider internal constructor(
    @Volatile var config: MapboxConfig,
    private val textDataSource: W3WTextDataSource,
    private val httpClient: HttpClient,
) : ResolvableSearchProvider {

    private val sessionManager by lazy {
        SessionManager(
            maxSuggestCalls = 50,
            sessionTimeoutSeconds = 180
        )
    }

    constructor(config: MapboxConfig, textDataSource: W3WTextDataSource) : this(
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

    override val providerId: String = MAPBOX_PROVIDER_ID

    /** Handles queries that meet or exceed [MapboxConfig.minQueryLength]. */
    override fun canHandle(query: String): Boolean = query.length >= config.minQueryLength

    /**
     * GETs `/search/searchbox/v1/suggest` and maps each suggestion to a [SearchResult.SearchSuggestion].
     *
     * The suggestion's [SearchResult.SearchSuggestion.extras] stores the `mapbox_id` so that
     * [resolve] can fetch precise coordinates via the retrieve endpoint.
     */
    override suspend fun executeSearch(query: String): W3WResult<List<SearchResult>> =
        withContext(Dispatchers.IO) {
            safeW3WCall {
                val snapshot = config
                if (sessionManager.shouldRefresh()) sessionManager.refresh()
                sessionManager.onSuggestCall()
                val response = httpClient.get(BASE_URL) {
                    url {
                        appendPathSegments(SUGGEST_PATH)
                    }
                    parameter(PARAM_SESSION_TOKEN, sessionManager.sessionToken)
                    parameter(PARAM_ACCESS_TOKEN, snapshot.apiKey)
                    parameter(PARAM_QUERY, query)
                    parameter(PARAM_LIMIT, snapshot.maxResults)
                    parameter(PARAM_LANGUAGE, snapshot.language.w3wCode)
                    if (snapshot.includedRegionCodes.isNotEmpty()) {
                        parameter(PARAM_COUNTRY, snapshot.includedRegionCodes.joinToString(","))
                    }
                    snapshot.focus?.let {
                        parameter(PARAM_PROXIMITY, "${it.lng},${it.lat}")
                    }
                }

                if (!response.status.isSuccess()) {
                    return@safeW3WCall W3WResult.Failure(response.toMapboxApiError())
                }

                val results = response.body<MapboxSearchResponse>().suggestions
                    .map { suggestion ->

                        val title = when (suggestion.featureType) {
                            "address" -> suggestion.address ?: suggestion.name
                            else -> suggestion.name
                        }
                        SearchResult.SearchSuggestion(
                            query = query,
                            providerId = providerId,
                            extras = buildMap {
                                put(EXTRAS_KEY_TITLE, title)
                                put(
                                    EXTRAS_KEY_SUBTITLE,
                                    suggestion.buildSubtitle() ?: suggestion.placeFormatted
                                )
                                put(EXTRAS_KEY_MAPBOX_ID, suggestion.mapboxId)
                                suggestion.distance?.let { distanceInMeters ->
                                    put(
                                        EXTRAS_KEY_DISTANCE_TO_FOCUS,
                                        distanceInMeters.div(1000).toString()
                                    )
                                }
                                put(
                                    EXTRAS_KEY_ZOOM_LEVEL,
                                    zoomLevelForFeatureType(suggestion.featureType).toString()
                                )
                            }
                        )
                    }
                W3WResult.Success(results)
            }
        }

    /**
     * Retrieves coordinates via `/search/searchbox/v1/retrieve/{mapbox_id}` and converts them
     * to a what3words address.
     *
     * @param data Suggestion produced by [executeSearch].
     * @return [W3WResult.Success] with a [SearchResult.ResolvedAddress], or [W3WResult.Failure]
     *   if the mapbox ID is missing, the retrieve call fails, or coordinate conversion fails.
     */
    override suspend fun resolve(data: SearchResult.SearchSuggestion): W3WResult<SearchResult.ResolvedAddress> =
        withContext(Dispatchers.IO) {
            safeW3WCall {
                val id = data.extras[EXTRAS_KEY_MAPBOX_ID]
                    ?: return@safeW3WCall W3WResult.Failure(MissingAddressIdException())

                val snapshot = config
                val response = httpClient.get(BASE_URL) {
                    url {
                        appendPathSegments(RETRIEVE_PATH, id)
                    }
                    parameter(PARAM_SESSION_TOKEN, sessionManager.sessionToken)
                    parameter(PARAM_ACCESS_TOKEN, snapshot.apiKey)
                }
                if (!response.status.isSuccess()) {
                    return@safeW3WCall W3WResult.Failure(response.toMapboxApiError())
                }

                sessionManager.refresh()
                val feature = response.body<MapboxRetrieveResponse>().features.firstOrNull()
                    ?: return@safeW3WCall W3WResult.Failure(
                        W3WError("No features returned from retrieve endpoint")
                    )

                val coordinates = feature.geometry.coordinates
                val lng = coordinates.getOrNull(0)
                val lat = coordinates.getOrNull(1)
                if (lng == null || lat == null) {
                    return@safeW3WCall W3WResult.Failure(InvalidCoordinatesException())
                }

                when (val w3wResult = textDataSource.convertTo3wa(
                    coordinates = W3WCoordinates(lat = lat, lng = lng),
                    language = snapshot.language,
                )) {
                    is W3WResult.Success -> W3WResult.Success(
                        SearchResult.ResolvedAddress(
                            query = data.query,
                            providerId = providerId,
                            address = w3wResult.value,
                        )
                    )

                    is W3WResult.Failure -> W3WResult.Failure(w3wResult.error, w3wResult.message)
                }
            }
        }
}

/**
 * Builds a concise subtitle from a [Suggestion]'s context and metadata.
 *
 * For **address** features the [Suggestion.address] is omitted (it would duplicate the name),
 * while for all other feature types it is included as the leading component.
 * The city ([Context.place]) and country — preferring the short ISO code
 * ([Country.countryCode], upper-cased) over the full country name — are appended.
 *
 * Example: for "Starbucks" with `featureType = "poi"`, `address = "10 Downing Street"`,
 * `place.name = "London"`, and `country.countryCode = "gb"`,
 * the result is **"10 Downing Street, London, GB"**.
 * For an address feature like "10 Downing Street" itself, the result is **"London, GB"**.
 *
 * @return The joined subtitle, or `null` if no components are available.
 */
private const val ZOOM_DEFAULT = 14

/**
 * Recommended map zoom level per Mapbox `feature_type`, aligned with the geographic
 * granularity scale used by other providers (country = wide, address = tight).
 */
private val FEATURE_TYPE_TO_ZOOM: Map<String, Int> = mapOf(
    "country" to 6,
    "region" to 6,
    "district" to 12,
    "postcode" to 19,
    "place" to 15,
    "locality" to 15,
    "neighborhood" to 15,
    "street" to 19,
    "address" to 19,
    "poi" to 16,
    "category" to 16,
)

/** Returns the recommended zoom level for the given Mapbox [featureType]. */
private fun zoomLevelForFeatureType(featureType: String): Int =
    FEATURE_TYPE_TO_ZOOM[featureType] ?: ZOOM_DEFAULT

private fun Suggestion.buildSubtitle(): String? {
    val address = when (featureType) {
        "address" -> null
        else -> address
    }
    val place = context.place?.name
    val country = context.country
    val countryLabel = country?.countryCode?.uppercase() ?: country?.name

    return listOfNotNull(address, place, countryLabel)
        .joinToString(", ")
        .ifEmpty { null }
}
