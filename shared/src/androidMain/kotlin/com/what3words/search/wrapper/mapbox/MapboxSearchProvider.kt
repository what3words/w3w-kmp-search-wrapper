package com.what3words.search.wrapper.mapbox

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WError
import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.search.wrapper.core.ResolvableSearchProvider
import com.what3words.search.wrapper.core.SearchResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.encodeURLPathPart
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/** Unique identifier for the Mapbox geocoding search provider. */
const val MAPBOX_PROVIDER_ID = "MapboxSearchProvider"

private const val BASE_URL = "https://api.mapbox.com/geocoding/v5/mapbox.places"
private const val PARAM_ACCESS_TOKEN = "access_token"
private const val PARAM_LIMIT = "limit"
private const val EXTRAS_KEY_LAT = "lat"
private const val EXTRAS_KEY_LNG = "lng"

/**
 * [ResolvableSearchProvider] backed by the Mapbox Geocoding API v5.
 *
 * Searches via `mapbox.places/{query}.json` and resolves suggestions to what3words addresses
 * by converting the coordinates returned in the initial response via
 * [W3WTextDataSource.convertTo3wa]. Because Mapbox includes coordinates in the search result,
 * [resolve] does **not** make an additional network request.
 *
 * @property config Provider configuration including API key, language, and result limits.
 * @property textDataSource Used for coordinate-to-what3words conversion.
 */
internal class MapboxSearchProvider internal constructor(
    private val config: MapboxConfig,
    private val textDataSource: W3WTextDataSource,
    private val httpClient: HttpClient,
) : ResolvableSearchProvider {

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
     * GETs `mapbox.places/{query}.json` and maps each feature to a [SearchResult.SearchSuggestion].
     *
     * Coordinates (`lat` / `lng`) are stored in [SearchResult.SearchSuggestion.extras] so that
     * [resolve] can convert them to a what3words address without an extra network round-trip.
     */
    override suspend fun executeSearch(query: String): W3WResult<List<SearchResult>> = withContext(Dispatchers.IO) {
        try {
            val encodedQuery = query.encodeURLPathPart()
            val response = httpClient.get("$BASE_URL/$encodedQuery.json") {
                parameter(PARAM_ACCESS_TOKEN, config.apiKey)
                parameter(PARAM_LIMIT, config.maxResults)
            }

            if (!response.status.isSuccess()) {
                return@withContext W3WResult.Failure(response.toMapboxApiError())
            }

            val results = response.body<MapboxFeatureCollection>().features
                .take(config.maxResults)
                .mapNotNull { feature ->
                    val lng = feature.center.getOrNull(0) ?: return@mapNotNull null
                    val lat = feature.center.getOrNull(1) ?: return@mapNotNull null

                    val subtitle = feature.placeName
                        .removePrefix("${feature.text}, ")
                        .takeIf { it != feature.placeName }

                    SearchResult.SearchSuggestion(
                        query = query,
                        providerId = providerId,
                        extras = buildMap {
                            put(EXTRAS_KEY_LAT, lat.toString())
                            put(EXTRAS_KEY_LNG, lng.toString())
                            put(SearchResult.SearchSuggestion.EXTRAS_KEY_TITLE, feature.text)
                            subtitle?.let { put(SearchResult.SearchSuggestion.EXTRAS_KEY_SUBTITLE, it) }
                        }
                    )
                }
            W3WResult.Success(results)
        } catch (e: Exception) {
            W3WResult.Failure(W3WError(e))
        }
    }

    /**
     * @param data Suggestion produced by [executeSearch].
     * @return [W3WResult.Success] with a [SearchResult.ResolvedAddress], or [W3WResult.Failure]
     *   if the coordinates are missing/invalid or the w3w conversion fails.
     */
    override suspend fun resolve(data: SearchResult.SearchSuggestion): W3WResult<SearchResult.ResolvedAddress> =
        withContext(Dispatchers.IO) {
            val lat = data.extras[EXTRAS_KEY_LAT]?.toDoubleOrNull()
                ?: return@withContext W3WResult.Failure(W3WError("Missing or invalid lat in suggestion extras"))
            val lng = data.extras[EXTRAS_KEY_LNG]?.toDoubleOrNull()
                ?: return@withContext W3WResult.Failure(W3WError("Missing or invalid lng in suggestion extras"))

            try {
                when (val w3wResult = textDataSource.convertTo3wa(
                    coordinates = W3WCoordinates(lat = lat, lng = lng),
                    language = config.language,
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
            } catch (e: Exception) {
                W3WResult.Failure(W3WError(e))
            }
        }
}
