package com.what3words.search.wrapper.googleplaces

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
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GooglePlacesSearchProviderTest {

    /** Base config with session tokens disabled for simpler tests. */
    private val defaultConfig = GooglePlacesConfig(
        apiKey = "test-key",
        useSessionTokens = false,
    )

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
        onRequest: (HttpRequestData) -> Unit = {},
        autocompleteBody: () -> String = { autocompleteJson() },
        detailsBody: () -> String = { detailsJson() },
    ): HttpClient {
        val engine = MockEngine { request ->
            onRequest(request)
            val body = if (request.url.encodedPath.contains("autocomplete")) {
                autocompleteBody()
            } else {
                detailsBody()
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
        config: GooglePlacesConfig = defaultConfig,
        dataSource: W3WTextDataSource = successDataSource,
        httpClient: HttpClient = mockClient(),
    ) = GooglePlacesSearchProvider(config, dataSource, httpClient)

    private fun autocompleteJson(
        placeId: String = "ChIJ_test",
        primaryText: String = "200 Main Street",
        secondaryText: String = "City, Country",
    ) = """
        {
            "suggestions": [{
                "placePrediction": {
                    "placeId": "$placeId",
                    "text": { "text": "$primaryText, $secondaryText" },
                    "structuredFormat": {
                        "mainText": { "text": "$primaryText" },
                        "secondaryText": { "text": "$secondaryText" }
                    }
                }
            }]
        }
    """.trimIndent()

    private fun autocompleteJsonWithN(n: Int) = buildString {
        append("""{ "suggestions": [""")
        repeat(n) { i ->
            if (i > 0) append(",")
            append("""{ "placePrediction": { "placeId": "place$i", "text": { "text": "Place $i" } } }""")
        }
        append("] }")
    }

    private fun detailsJson(
        placeId: String = "ChIJ_test",
        lat: Double = 37.422054,
        lng: Double = -122.085324,
    ) = """
        {
            "id": "$placeId",
            "formattedAddress": "200 Main Street, City, Country",
            "location": { "latitude": $lat, "longitude": $lng },
            "displayName": { "text": "Test Place" }
        }
    """.trimIndent()

    private fun apiErrorJson(
        code: Int = 403,
        status: String = "PERMISSION_DENIED",
        message: String = "Requests from this client are blocked.",
    ) = """
        {
            "error": {
                "code": $code,
                "message": "$message",
                "status": "$status"
            }
        }
    """.trimIndent()

    private fun suggestionWith(placeId: String = "ChIJ_test") =
        SearchResult.SearchSuggestion(
            query = "test",
            providerId = GOOGLE_PLACES_PROVIDER_ID,
            title = "",
            extras = mapOf("placeId" to placeId),
        )

    private fun readBodyText(request: HttpRequestData): String =
        when (val body = request.body) {
            is OutgoingContent.ByteArrayContent -> body.bytes().decodeToString()
            else -> ""
        }

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

    @Test
    fun executeSearch_mapsStructuredFormatToTitleSubtitleAndExtras() = runTest {
        val result = provider().executeSearch("main")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        val suggestion = assertIs<SearchResult.SearchSuggestion>(result.value.first())
        assertEquals(GOOGLE_PLACES_PROVIDER_ID, suggestion.providerId)
        assertEquals("200 Main Street", suggestion.title)
        assertEquals("City, Country", suggestion.subtitle)
        assertEquals("ChIJ_test", suggestion.extras["placeId"])
    }

    @Test
    fun executeSearch_fallsBackToTextFieldWhenStructuredFormatIsAbsent() = runTest {
        val noStructure = """
            {
                "suggestions": [{
                    "placePrediction": {
                        "placeId": "place1",
                        "text": { "text": "Fallback full text" }
                    }
                }]
            }
        """.trimIndent()

        val result = provider(
            httpClient = mockClient(autocompleteBody = { noStructure })
        ).executeSearch("main")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        val suggestion = assertIs<SearchResult.SearchSuggestion>(result.value.first())
        assertEquals("Fallback full text", suggestion.title)
        assertNull(suggestion.subtitle)
    }

    @Test
    fun executeSearch_filtersOutSuggestionsWithNullPlacePrediction() = runTest {
        val json = """{ "suggestions": [{ "placePrediction": null }] }"""

        val result = provider(
            httpClient = mockClient(autocompleteBody = { json })
        ).executeSearch("main")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(0, result.value.size)
    }

    @Test
    fun executeSearch_returnsEmptyListForEmptySuggestionsArray() = runTest {
        val result = provider(
            httpClient = mockClient(autocompleteBody = { """{ "suggestions": [] }""" })
        ).executeSearch("main")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(0, result.value.size)
    }

    @Test
    fun executeSearch_limitsResultsByMaxResults() = runTest {
        // API returns 8 suggestions; maxResults caps at 3.
        val p = provider(
            config = defaultConfig.copy(maxResults = 3),
            httpClient = mockClient(autocompleteBody = { autocompleteJsonWithN(8) }),
        )

        val result = p.executeSearch("main")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(3, result.value.size)
    }

    @Test
    fun executeSearch_returnsFailureOnNetworkError() = runTest {
        val engine = MockEngine { respondError(HttpStatusCode.InternalServerError) }
        val httpClient = HttpClient(engine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        }

        val result = provider(httpClient = httpClient).executeSearch("main")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
    }

    @Test
    fun executeSearch_returnsGooglePlacesApiErrorWithParsedFields() = runTest {
        val p = provider(
            httpClient = mockClientWithStatus(
                status = HttpStatusCode.Forbidden,
                body = apiErrorJson(code = 403, status = "PERMISSION_DENIED", message = "API key blocked."),
            )
        )

        val result = p.executeSearch("main")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        val error = assertIs<GooglePlacesApiError>(result.error)
        assertEquals(403, error.code)
        assertEquals("PERMISSION_DENIED", error.status)
        assertEquals("API key blocked.", error.apiMessage)
    }

    @Test
    fun executeSearch_includesSessionTokenInRequestBodyWhenEnabled() = runTest {
        var capturedBody = ""
        val p = provider(
            config = defaultConfig.copy(useSessionTokens = true),
            httpClient = mockClient(onRequest = { capturedBody = readBodyText(it) }),
        )

        p.executeSearch("main")

        assertTrue(
            "sessionToken" in capturedBody,
            "Expected body to contain sessionToken, was: $capturedBody",
        )
    }

    @Test
    fun executeSearch_omitsSessionTokenFromBodyWhenDisabled() = runTest {
        var capturedBody = ""
        val p = provider(
            config = defaultConfig.copy(useSessionTokens = false),
            httpClient = mockClient(onRequest = { capturedBody = readBodyText(it) }),
        )

        p.executeSearch("main")

        assertFalse(
            "sessionToken" in capturedBody,
            "Expected no sessionToken in body, was: $capturedBody",
        )
    }

    @Test
    fun resolve_returnsFailureWhenPlaceIdMissingFromExtras() = runTest {
        val suggestion = SearchResult.SearchSuggestion(
            query = "test",
            providerId = GOOGLE_PLACES_PROVIDER_ID,
            title = "",
            extras = emptyMap(),
        )

        val result = provider().resolve(suggestion)

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
        val msg = result.error.message
        assertNotNull(msg)
        assertTrue(msg.contains("placeId"))
    }

    @Test
    fun resolve_returnsResolvedAddressOnSuccess() = runTest {
        val result = provider().resolve(suggestionWith("ChIJ_test"))

        assertIs<W3WResult.Success<SearchResult.ResolvedAddress>>(result)
        assertEquals(GOOGLE_PLACES_PROVIDER_ID, result.value.providerId)
        assertEquals("test", result.value.query)
        assertEquals(fakeAddress().words, result.value.address.words)
    }

    @Test
    fun resolve_returnsFailureWhenLocationIsNullInDetailsResponse() = runTest {
        val noLocation = """{ "id": "ChIJ_test", "formattedAddress": "Main St" }"""

        val result = provider(
            httpClient = mockClient(detailsBody = { noLocation })
        ).resolve(suggestionWith())

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
        val msg = result.error.message
        assertNotNull(msg)
        assertTrue(msg.contains("location"))
    }

    @Test
    fun resolve_propagatesW3WTextDataSourceFailure() = runTest {
        val failingSource = dataSourceReturning(W3WResult.Failure(W3WError("conversion failed")))

        val result = provider(dataSource = failingSource).resolve(suggestionWith())

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
        assertEquals("conversion failed", result.error.message)
    }

    @Test
    fun resolve_returnsFailureOnNetworkError() = runTest {
        val engine = MockEngine { respondError(HttpStatusCode.NotFound) }
        val httpClient = HttpClient(engine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        }

        val result = provider(httpClient = httpClient).resolve(suggestionWith())

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
    }

    @Test
    fun resolve_returnsGooglePlacesApiErrorWithParsedFields() = runTest {
        val p = provider(
            httpClient = mockClientWithStatus(
                status = HttpStatusCode.Forbidden,
                body = apiErrorJson(code = 403, status = "PERMISSION_DENIED", message = "API key blocked."),
            )
        )

        val result = p.resolve(suggestionWith())

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
        val error = assertIs<GooglePlacesApiError>(result.error)
        assertEquals(403, error.code)
        assertEquals("PERMISSION_DENIED", error.status)
        assertEquals("API key blocked.", error.apiMessage)
    }

    @Test
    fun resolve_includesSessionTokenAsQueryParamWhenEnabled() = runTest {
        var capturedToken: String? = null
        val p = provider(
            config = defaultConfig.copy(useSessionTokens = true),
            httpClient = mockClient(onRequest = { req ->
                if (!req.url.encodedPath.contains("autocomplete")) {
                    capturedToken = req.url.parameters["sessionToken"]
                }
            }),
        )

        p.resolve(suggestionWith())

        assertNotNull(capturedToken, "Expected sessionToken query param on details request")
    }

    @Test
    fun resolve_omitsSessionTokenQueryParamWhenDisabled() = runTest {
        var capturedToken: String? = "sentinel"
        val p = provider(
            config = defaultConfig.copy(useSessionTokens = false),
            httpClient = mockClient(onRequest = { req ->
                if (!req.url.encodedPath.contains("autocomplete")) {
                    capturedToken = req.url.parameters["sessionToken"]
                }
            }),
        )

        p.resolve(suggestionWith())

        assertNull(capturedToken)
    }

    @Test
    fun resolve_rotatesSessionTokenBetweenCalls() = runTest {
        val detailTokens = mutableListOf<String>()
        val p = provider(
            config = defaultConfig.copy(useSessionTokens = true),
            httpClient = mockClient(onRequest = { req ->
                if (!req.url.encodedPath.contains("autocomplete")) {
                    req.url.parameters["sessionToken"]?.let { detailTokens.add(it) }
                }
            }),
        )

        p.resolve(suggestionWith()) // uses token T1, then rotates to T2
        p.resolve(suggestionWith()) // uses token T2

        assertEquals(2, detailTokens.size)
        assertNotEquals(
            detailTokens[0],
            detailTokens[1],
            "Session token should rotate after each resolve call",
        )
    }

    @Test
    fun resolve_rotatesSessionTokenEvenAfterFailure() = runTest {
        val detailTokens = mutableListOf<String>()
        val noLocation = """{ "id": "ChIJ_test", "formattedAddress": "Main St" }"""

        val p = provider(
            config = defaultConfig.copy(useSessionTokens = true),
            httpClient = mockClient(
                onRequest = { req ->
                    if (!req.url.encodedPath.contains("autocomplete")) {
                        req.url.parameters["sessionToken"]?.let { detailTokens.add(it) }
                    }
                },
                detailsBody = { noLocation },
            ),
        )

        p.resolve(suggestionWith()) // fails (no location), but token should still rotate
        p.resolve(suggestionWith()) // uses a new token

        assertEquals(2, detailTokens.size)
        assertNotEquals(
            detailTokens[0],
            detailTokens[1],
            "Session token should rotate even when resolve fails",
        )
    }

}
