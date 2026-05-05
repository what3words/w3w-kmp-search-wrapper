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
import com.what3words.search.wrapper.error.MissingAddressIdException
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

    private fun mockClient(
        suggestBody: () -> String = { suggestionsJson() },
        retrieveBody: () -> String = { retrieveResponseJson() },
    ): HttpClient {
        val engine = MockEngine { request ->
            val body = when {
                request.url.encodedPath.contains("retrieve") -> retrieveBody()
                else -> suggestBody()
            }
            respond(
                content = body,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }
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

    /** Builds a Mapbox Search Box API v1 suggestion response with one suggestion. */
    private fun suggestionsJson(
        mapboxId: String = "mapbox_id.123",
        name: String = "Hanoi",
        featureType: String = "place",
        placeFormatted: String = "Hanoi, Vietnam",
        address: String? = null,
        language: String = "en",
        countryCode: String? = "vn",
        countryName: String? = "Vietnam",
        placeName: String? = null,
        distance: Int? = null,
    ): String {
        val addressField = if (address != null) """, "address": "$address"""" else ""
        val distanceField = if (distance != null) """, "distance": $distance""" else ""
        val countryFields = listOfNotNull(
            countryCode?.let { """"country_code": "$it"""" },
            countryName?.let { """"name": "$it"""" },
        ).joinToString(", ").let { if (it.isNotEmpty()) "{$it}" else "{}" }
        val placeField = if (placeName != null) """, "place": {"name": "$placeName"}""" else ""
        return """
            {
                "suggestions": [{
                    "name": "$name",
                    "mapbox_id": "$mapboxId",
                    "feature_type": "$featureType",
                    "place_formatted": "$placeFormatted",
                    "language": "$language"$addressField$distanceField,
                    "context": {
                        "country": $countryFields$placeField
                    }
                }]
            }
        """.trimIndent()
    }

    /** Builds a suggestion response with [n] suggestions. */
    private fun suggestionsJsonWithN(n: Int) = buildString {
        append("""{ "suggestions": [""")
        repeat(n) { i ->
            if (i > 0) append(",")
            append("""{ "name": "Place $i", "mapbox_id": "mapbox_id.$i", "feature_type": "place", "place_formatted": "Place $i, Country", "language": "en", "context": { "country": {"country_code": "xx", "name": "Country"} } }""")
        }
        append("] }")
    }

    /** Builds a Mapbox API error response body. */
    private fun apiErrorJson(message: String = "Not Authorized - No token was found") =
        """{ "message": { "error": "$message" } }"""

    /** Builds a retrieve response with coordinates. */
    private fun retrieveResponseJson(lng: Double = 105.8412, lat: Double = 21.0245) =
        """{ "features": [{ "geometry": { "coordinates": [$lng, $lat] } }] }"""

    /** Creates a [SearchResult.SearchSuggestion] with a mapbox_id extra. */
    private fun suggestionWith(mapboxId: String = "mapbox_id.123") =
        SearchResult.SearchSuggestion(
            query = "hanoi",
            providerId = MAPBOX_PROVIDER_ID,
            extras = mapOf(
                SearchResult.EXTRAS_KEY_TITLE to "Hanoi",
                "mapbox_id" to mapboxId,
            ),
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
        assertEquals("VN", suggestion.subtitle)
        assertNotNull(suggestion.extras["mapbox_id"])
    }

    @Test
    fun executeSearch_derivesSubtitleFromContext() = runTest {
        // context has place = "Paris" and country country_code = "fr"
        // → subtitle should be "Paris, FR"
        val body = suggestionsJson(
            name = "Eiffel Tower",
            featureType = "place",
            placeFormatted = "Eiffel Tower, Paris, France",
            countryCode = "fr",
            countryName = "France",
            placeName = "Paris",
        )
        val result = provider(httpClient = mockClient(suggestBody = { body })).executeSearch("eiffel")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        val suggestion = assertIs<SearchResult.SearchSuggestion>(result.value.first())
        assertEquals("Eiffel Tower", suggestion.title)
        assertEquals("Paris, FR", suggestion.subtitle)
    }

    @Test
    fun executeSearch_fallsBackToPlaceFormattedWhenContextIsEmpty() = runTest {
        // When context has no country or place entries, subtitle falls back to placeFormatted.
        val body = suggestionsJson(
            name = "Hanoi",
            placeFormatted = "Hanoi",
            countryCode = null,
            countryName = null,
            placeName = null,
        )
        val result = provider(httpClient = mockClient(suggestBody = { body })).executeSearch("hanoi")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        val suggestion = assertIs<SearchResult.SearchSuggestion>(result.value.first())
        assertEquals("Hanoi", suggestion.subtitle)
    }

    @Test
    fun executeSearch_storesMapboxIdInExtras() = runTest {
        val result = provider().executeSearch("hanoi")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        val suggestion = assertIs<SearchResult.SearchSuggestion>(result.value.first())
        assertEquals("mapbox_id.123", suggestion.extras["mapbox_id"])
    }

    @Test
    fun executeSearch_returnsAllSuggestionsFromResponse() = runTest {
        val body = """
            {
                "suggestions": [
                    { "name": "First", "mapbox_id": "mapbox_id.1", "feature_type": "place", "place_formatted": "First, Country", "language": "en", "context": { "country": {"country_code": "xx", "name": "Country"} } },
                    { "name": "Second", "mapbox_id": "mapbox_id.2", "feature_type": "place", "place_formatted": "Second, Country", "language": "en", "context": { "country": {"country_code": "xx", "name": "Country"} } }
                ]
            }
        """.trimIndent()
        val result = provider(httpClient = mockClient(suggestBody = { body })).executeSearch("query")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(2, result.value.size)
        val first = assertIs<SearchResult.SearchSuggestion>(result.value[0])
        assertEquals("First", first.title)
        val second = assertIs<SearchResult.SearchSuggestion>(result.value[1])
        assertEquals("Second", second.title)
    }

    @Test
    fun executeSearch_returnsEmptyListForEmptySuggestionsArray() = runTest {
        val result = provider(
            httpClient = mockClient(suggestBody = { """{ "suggestions": [] }""" })
        ).executeSearch("query")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(0, result.value.size)
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
    fun executeSearch_includesAddressInTitleWhenPresent() = runTest {
        val body = suggestionsJson(
            name = "Gipsy Hill",
            featureType = "address",
            address = "49",
            placeFormatted = "49 Gipsy Hill, London, England, United Kingdom",
            countryCode = "gb",
            countryName = "United Kingdom",
            placeName = "London",
        )
        val result = provider(httpClient = mockClient(suggestBody = { body })).executeSearch("49 gipsy hill")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        val suggestion = assertIs<SearchResult.SearchSuggestion>(result.value.first())
        assertEquals("49", suggestion.title)
        assertEquals("London, GB", suggestion.subtitle)
    }

    @Test
    fun executeSearch_usesNameWhenAddressIsNull() = runTest {
        val body = suggestionsJson(
            name = "Gipsy Hill",
            featureType = "address",
            address = null,
            placeFormatted = "Gipsy Hill, London, England, United Kingdom",
            countryCode = "gb",
            countryName = "United Kingdom",
            placeName = "London",
        )
        val result = provider(httpClient = mockClient(suggestBody = { body })).executeSearch("gipsy hill")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        val suggestion = assertIs<SearchResult.SearchSuggestion>(result.value.first())
        assertEquals("Gipsy Hill", suggestion.title)
        assertEquals("London, GB", suggestion.subtitle)
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
    fun resolve_returnsFailureWhenMapboxIdMissingFromExtras() = runTest {
        val suggestion = SearchResult.SearchSuggestion(
            query = "hanoi",
            providerId = MAPBOX_PROVIDER_ID,
            extras = mapOf("some_key" to "some_value"),  // no mapbox_id
        )

        val result = provider().resolve(suggestion)

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
        val error = assertIs<MissingAddressIdException>(result.error)
        val msg = error.message
        assertNotNull(msg)
        assertTrue(msg.contains("Missing address id"))
    }

    @Test
    fun resolve_returnsFailureWhenExtrasAreEmpty() = runTest {
        val suggestion = SearchResult.SearchSuggestion(
            query = "hanoi",
            providerId = MAPBOX_PROVIDER_ID,
            extras = emptyMap(),
        )

        val result = provider().resolve(suggestion)

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
        assertIs<MissingAddressIdException>(result.error)
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
    fun resolve_makesOneHttpRequestToRetrieveEndpoint() = runTest {
        // Verify resolve calls the retrieve endpoint exactly once.
        var requestCount = 0
        var lastRequestPath: String? = null
        val engine = MockEngine { request ->
            requestCount++
            lastRequestPath = request.url.encodedPath
            respond(
                content = retrieveResponseJson(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }
        val httpClient = HttpClient(engine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true; isLenient = true }) }
        }

        provider(httpClient = httpClient).resolve(suggestionWith())

        assertEquals(1, requestCount, "resolve should make exactly one HTTP request")
        assertNotNull(lastRequestPath)
        assertTrue(lastRequestPath.contains("retrieve"), "Request should be to retrieve endpoint")
    }
}
