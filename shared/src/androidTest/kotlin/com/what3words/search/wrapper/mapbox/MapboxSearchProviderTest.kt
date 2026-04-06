package com.what3words.search.wrapper.mapbox

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WError
import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.domain.W3WAddress
import com.what3words.core.types.domain.W3WSuggestion
import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.core.types.geometry.W3WGridSection
import com.what3words.core.types.geometry.W3WRectangle
import com.what3words.core.types.language.W3WLanguage
import com.what3words.core.types.language.W3WProprietaryLanguage
import com.what3words.core.types.options.W3WAutosuggestOptions
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.fixtures.fakeAddress
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MapboxSearchProviderTest {

    private val defaultConfig = MapboxConfig(apiKey = "test-token")

    private val successDataSource = dataSourceReturning(W3WResult.Success(fakeAddress()))

    private fun dataSourceReturning(result: W3WResult<W3WAddress>) =
        object : W3WTextDataSource {
            override fun version(version: W3WTextDataSource.Version): String? = null
            override fun convertTo3wa(coordinates: W3WCoordinates, language: W3WLanguage) = result
            override fun convertToCoordinates(words: String): W3WResult<W3WAddress> =
                throw NotImplementedError()

            override fun autosuggest(input: String, options: W3WAutosuggestOptions?): W3WResult<List<W3WSuggestion>> =
                throw NotImplementedError()

            override fun gridSection(boundingBox: W3WRectangle): W3WResult<W3WGridSection> =
                throw NotImplementedError()

            override fun availableLanguages(): W3WResult<Set<W3WProprietaryLanguage>> =
                throw NotImplementedError()

            override fun isValid3wa(words: String): W3WResult<Boolean> =
                throw NotImplementedError()
        }

    private fun mockClient(body: () -> String = { featureCollectionJson() }): HttpClient {
        val engine = MockEngine { respond(
            content = body(),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
        ) }
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; isLenient = true })
            }
        }
    }

    private fun mockClientWithStatus(
        status: HttpStatusCode,
        body: String = "",
    ): HttpClient {
        val engine = MockEngine { _ ->
            respond(
                content = body,
                status = status,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; isLenient = true })
            }
        }
    }

    private fun provider(
        config: MapboxConfig = defaultConfig,
        dataSource: W3WTextDataSource = successDataSource,
        httpClient: HttpClient = mockClient(),
    ) = MapboxSearchProvider(config, dataSource, httpClient)

    /** Builds a valid GeoJSON feature collection with one feature. */
    private fun featureCollectionJson(
        id: String = "place.123",
        text: String = "Hanoi",
        placeName: String = "Hanoi, Vietnam",
        lng: Double = 105.8412,
        lat: Double = 21.0245,
    ) = """
        {
            "features": [{
                "id": "$id",
                "text": "$text",
                "place_name": "$placeName",
                "center": [$lng, $lat],
                "context": [{"id": "country.1", "text": "Vietnam"}]
            }]
        }
    """.trimIndent()

    /** Builds a feature collection with [n] features. */
    private fun featureCollectionWithN(n: Int) = buildString {
        append("""{ "features": [""")
        repeat(n) { i ->
            if (i > 0) append(",")
            append("""{ "id": "place.$i", "text": "Place $i", "place_name": "Place $i, Country", "center": [${i.toDouble()}, ${i.toDouble()}], "context": [] }""")
        }
        append("] }")
    }

    /** Builds a Mapbox API error response body. */
    private fun apiErrorJson(message: String = "Not Authorized - No token was found") =
        """{ "message": "$message" }"""

    /** Creates a [SearchResult.SearchSuggestion] with lat/lng extras. */
    private fun suggestionWith(lat: Double = 21.0245, lng: Double = 105.8412) =
        SearchResult.SearchSuggestion(
            query = "hanoi",
            providerId = MAPBOX_PROVIDER_ID,
            title = "Hanoi",
            extras = mapOf("lat" to lat.toString(), "lng" to lng.toString()),
        )

    // ── canHandle ────────────────────────────────────────────────────────────

    @Test
    fun canHandle_returnsFalseForBlankQuery() {
        val p = provider()
        assertFalse(p.canHandle(""))
        assertFalse(p.canHandle("  "))
    }

    @Test
    fun canHandle_returnsFalseForQueryShorterThanMinQueryLength() {
        val p = provider()
        assertFalse(p.canHandle("a"))
        assertFalse(p.canHandle("ab"))
    }

    @Test
    fun canHandle_returnsTrueForQueryAtLeastMinQueryLength() {
        val p = provider()
        assertTrue(p.canHandle("abc"))
        assertTrue(p.canHandle("123 Main Street"))
    }

    @Test
    fun canHandle_respectsCustomMinQueryLength() {
        val p = provider(config = defaultConfig.copy(minQueryLength = 5))
        assertFalse(p.canHandle("abcd"))   // 4 chars — below threshold
        assertTrue(p.canHandle("abcde"))   // 5 chars — at threshold
        assertTrue(p.canHandle("abcdef"))  // 6 chars — above threshold
    }

    // ── executeSearch ─────────────────────────────────────────────────────────

    @Test
    fun executeSearch_mapsFeatureToTitleSubtitleAndExtras() = runTest {
        val result = provider().executeSearch("hanoi")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        val suggestion = assertIs<SearchResult.SearchSuggestion>(result.value.first())
        assertEquals(MAPBOX_PROVIDER_ID, suggestion.providerId)
        assertEquals("Hanoi", suggestion.title)
        assertEquals("Vietnam", suggestion.subtitle)
        assertNotNull(suggestion.extras["lat"])
        assertNotNull(suggestion.extras["lng"])
    }

    @Test
    fun executeSearch_derivesSubtitleByStrippingTextPrefixFromPlaceName() = runTest {
        // place_name = "Eiffel Tower, Paris, France", text = "Eiffel Tower"
        // → subtitle should be "Paris, France"
        val body = featureCollectionJson(
            text = "Eiffel Tower",
            placeName = "Eiffel Tower, Paris, France",
            lng = 2.2945,
            lat = 48.8584,
        )
        val result = provider(httpClient = mockClient(body = { body })).executeSearch("eiffel")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        val suggestion = assertIs<SearchResult.SearchSuggestion>(result.value.first())
        assertEquals("Eiffel Tower", suggestion.title)
        assertEquals("Paris, France", suggestion.subtitle)
    }

    @Test
    fun executeSearch_setsNullSubtitleWhenPlaceNameMatchesText() = runTest {
        // When place_name == text there is no parent context to strip.
        val body = featureCollectionJson(text = "Hanoi", placeName = "Hanoi", lng = 105.84, lat = 21.02)
        val result = provider(httpClient = mockClient(body = { body })).executeSearch("hanoi")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        val suggestion = assertIs<SearchResult.SearchSuggestion>(result.value.first())
        assertNull(suggestion.subtitle)
    }

    @Test
    fun executeSearch_storesCoordinatesInExtras() = runTest {
        val result = provider().executeSearch("hanoi")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        val suggestion = assertIs<SearchResult.SearchSuggestion>(result.value.first())
        // center is [lng, lat]; verify both are stored as strings
        assertEquals("21.0245", suggestion.extras["lat"])
        assertEquals("105.8412", suggestion.extras["lng"])
    }

    @Test
    fun executeSearch_skipsFeatureWithMissingCenterCoordinates() = runTest {
        // A feature with an empty center array should be silently dropped.
        val body = """
            {
                "features": [
                    { "id": "place.1", "text": "Valid", "place_name": "Valid, Country", "center": [1.0, 2.0], "context": [] },
                    { "id": "place.2", "text": "NoCentre", "place_name": "NoCentre", "center": [], "context": [] }
                ]
            }
        """.trimIndent()
        val result = provider(httpClient = mockClient(body = { body })).executeSearch("query")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        val suggestion = assertIs<SearchResult.SearchSuggestion>(result.value.first())
        assertEquals("Valid", suggestion.title)
    }

    @Test
    fun executeSearch_returnsEmptyListForEmptyFeaturesArray() = runTest {
        val result = provider(
            httpClient = mockClient(body = { """{ "features": [] }""" })
        ).executeSearch("query")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(0, result.value.size)
    }

    @Test
    fun executeSearch_limitsResultsByMaxResults() = runTest {
        // API returns 8 features; maxResults caps at 3.
        val p = provider(
            config = defaultConfig.copy(maxResults = 3),
            httpClient = mockClient(body = { featureCollectionWithN(8) }),
        )

        val result = p.executeSearch("query")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(3, result.value.size)
    }

    @Test
    fun executeSearch_returnsFailureOnNetworkError() = runTest {
        val engine = MockEngine { respondError(HttpStatusCode.InternalServerError) }
        val httpClient = HttpClient(engine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        }

        val result = provider(httpClient = httpClient).executeSearch("query")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
    }

    @Test
    fun executeSearch_returnsMapboxApiErrorWithParsedMessage() = runTest {
        val p = provider(
            httpClient = mockClientWithStatus(
                status = HttpStatusCode.Unauthorized,
                body = apiErrorJson("Not Authorized - No token was found"),
            )
        )

        val result = p.executeSearch("query")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        val error = assertIs<MapboxApiError>(result.error)
        assertEquals(HttpStatusCode.Unauthorized.value, error.httpStatus)
        assertEquals("Not Authorized - No token was found", error.apiMessage)
    }

    // ── resolve ───────────────────────────────────────────────────────────────

    @Test
    fun resolve_returnsFailureWhenLatMissingFromExtras() = runTest {
        val suggestion = SearchResult.SearchSuggestion(
            query = "hanoi",
            providerId = MAPBOX_PROVIDER_ID,
            title = "Hanoi",
            extras = mapOf("lng" to "105.8412"),  // no lat
        )

        val result = provider().resolve(suggestion)

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
        val msg = result.error.message
        assertNotNull(msg)
        assertTrue(msg.contains("lat"))
    }

    @Test
    fun resolve_returnsFailureWhenLngMissingFromExtras() = runTest {
        val suggestion = SearchResult.SearchSuggestion(
            query = "hanoi",
            providerId = MAPBOX_PROVIDER_ID,
            title = "Hanoi",
            extras = mapOf("lat" to "21.0245"),  // no lng
        )

        val result = provider().resolve(suggestion)

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
        val msg = result.error.message
        assertNotNull(msg)
        assertTrue(msg.contains("lng"))
    }

    @Test
    fun resolve_returnsFailureWhenExtrasAreEmpty() = runTest {
        val suggestion = SearchResult.SearchSuggestion(
            query = "hanoi",
            providerId = MAPBOX_PROVIDER_ID,
            title = "Hanoi",
            extras = emptyMap(),
        )

        val result = provider().resolve(suggestion)

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
    }

    @Test
    fun resolve_returnsResolvedAddressOnSuccess() = runTest {
        val result = provider().resolve(suggestionWith())

        assertIs<W3WResult.Success<SearchResult.ResolvedAddress>>(result)
        assertEquals(MAPBOX_PROVIDER_ID, result.value.providerId)
        assertEquals("hanoi", result.value.query)
        assertEquals(fakeAddress().words, result.value.address.words)
    }

    @Test
    fun resolve_propagatesW3WTextDataSourceFailure() = runTest {
        val failingSource = dataSourceReturning(W3WResult.Failure(W3WError("conversion failed")))

        val result = provider(dataSource = failingSource).resolve(suggestionWith())

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
        assertEquals("conversion failed", result.error.message)
    }

    @Test
    fun resolve_doesNotMakeNetworkRequest() = runTest {
        // Verify resolve uses extras from the suggestion and never calls the HTTP client.
        var requestCount = 0
        val engine = MockEngine {
            requestCount++
            respond(
                content = featureCollectionJson(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }
        val httpClient = HttpClient(engine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true; isLenient = true }) }
        }

        provider(httpClient = httpClient).resolve(suggestionWith())

        assertEquals(0, requestCount, "resolve should not make any HTTP requests")
    }
}
