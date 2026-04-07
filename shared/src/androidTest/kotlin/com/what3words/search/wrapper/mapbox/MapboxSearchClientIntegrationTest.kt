package com.what3words.search.wrapper.mapbox

import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.W3WSearchClient
import com.what3words.search.wrapper.error.ProviderNotFoundException
import com.what3words.search.wrapper.error.ProviderNotResolvableException
import com.what3words.search.wrapper.fake.FakeW3WTextDataSource
import com.what3words.search.wrapper.fake.fakeProvider
import com.what3words.search.wrapper.fixtures.fakeAddress
import com.what3words.search.wrapper.fixtures.pluginFor
import com.what3words.search.wrapper.fixtures.suggestion
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
import kotlin.test.assertIs

class MapboxSearchClientIntegrationTest {

    private val defaultConfig = MapboxConfig(apiKey = "test-token")
    private val defaultDataSource = FakeW3WTextDataSource()

    private fun buildClient(
        config: MapboxConfig = defaultConfig,
        httpClient: HttpClient = successMockClient(),
        mapboxPriority: Int = 10,
        fallbackProvider: Pair<String, Int>? = null,
    ): W3WSearchClient {
        val mapboxProvider = MapboxSearchProvider(config, defaultDataSource, httpClient)
        return W3WSearchClient(defaultDataSource) {
            install(pluginFor(mapboxProvider), priority = mapboxPriority)
            fallbackProvider?.let { (id, priority) ->
                install(
                    pluginFor(fakeProvider(id, results = listOf(suggestion(id)))),
                    priority = priority,
                )
            }
        }
    }

    private fun successMockClient(): HttpClient {
        val engine = MockEngine {
            respond(
                content = featureCollectionJson(),
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

    private fun errorMockClient(status: HttpStatusCode = HttpStatusCode.Unauthorized): HttpClient {
        val engine = MockEngine { respondError(status) }
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; isLenient = true })
            }
        }
    }

    private fun featureCollectionJson() = """
        {
            "features": [{
                "id": "place.123",
                "text": "Hanoi",
                "place_name": "Hanoi, Vietnam",
                "center": [105.8412, 21.0245],
                "context": [{"id": "country.1", "text": "Vietnam"}]
            }]
        }
    """.trimIndent()

    // ── search ────────────────────────────────────────────────────────────────

    @Test
    fun search_withLongEnoughQuery_returnsSuggestionsFromMapbox() = runTest {
        val client = buildClient()

        val result = client.search("Hanoi Vietnam")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        val suggestion = assertIs<SearchResult.SearchSuggestion>(result.value.first())
        assertEquals(MAPBOX_PROVIDER_ID, suggestion.providerId)
        assertEquals("Hanoi", suggestion.title)
        assertEquals("Vietnam", suggestion.subtitle)
    }

    @Test
    fun search_withQueryBelowMinLength_fallsThrough_toFallbackProvider() = runTest {
        // "ab" is 2 chars — below the default minQueryLength of 3.
        val client = buildClient(fallbackProvider = "fallback" to 1)

        val result = client.search("ab")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        assertEquals("fallback", result.value.first().providerId)
    }

    @Test
    fun search_withQueryBelowMinLength_andNoFallback_returnsProviderNotFound() = runTest {
        val client = buildClient()

        val result = client.search("ab")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        assertIs<ProviderNotFoundException>(result.error)
    }

    @Test
    fun search_withCustomMinQueryLength_respectsThreshold() = runTest {
        // minQueryLength = 5: "abcd" (4 chars) should fall to fallback; "abcde" (5) should reach Mapbox.
        val config = defaultConfig.copy(minQueryLength = 5)
        val client = buildClient(
            config = config,
            fallbackProvider = "fallback" to 1,
        )

        val shortResult = client.search("abcd")
        assertIs<W3WResult.Success<List<SearchResult>>>(shortResult)
        assertEquals("fallback", shortResult.value.first().providerId)

        val longResult = client.search("abcde")
        assertIs<W3WResult.Success<List<SearchResult>>>(longResult)
        assertEquals(MAPBOX_PROVIDER_ID, longResult.value.first().providerId)
    }

    @Test
    fun search_whenApiReturnsError_propagatesFailure() = runTest {
        val client = buildClient(httpClient = errorMockClient(HttpStatusCode.Unauthorized))

        val result = client.search("Hanoi Vietnam")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        assertIs<MapboxApiError>(result.error)
    }

    @Test
    fun search_respectsMaxResults_cappingReturnedSuggestions() = runTest {
        val manyResults = buildString {
            append("""{ "features": [""")
            repeat(10) { i ->
                if (i > 0) append(",")
                append("""{ "id": "place.$i", "text": "Place $i", "place_name": "Place $i, Country", "center": [${i.toDouble()}, ${i.toDouble()}], "context": [] }""")
            }
            append("] }")
        }
        val client = buildClient(
            config = defaultConfig.copy(maxResults = 3),
            httpClient = HttpClient(MockEngine { respond(
                content = manyResults,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            ) }) {
                install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true; isLenient = true }) }
            },
        )

        val result = client.search("place")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(3, result.value.size)
    }

    // ── resolve ───────────────────────────────────────────────────────────────

    @Test
    fun resolve_delegatesToMapboxSearchProvider_andReturnsAddress() = runTest {
        val client = buildClient()
        val suggestion = SearchResult.SearchSuggestion(
            query = "Hanoi",
            providerId = MAPBOX_PROVIDER_ID,
            title = "Hanoi",
            extras = mapOf("lat" to "21.0245", "lng" to "105.8412"),
        )

        val result = client.resolve(suggestion)

        assertIs<W3WResult.Success<SearchResult.ResolvedAddress>>(result)
        assertEquals(MAPBOX_PROVIDER_ID, result.value.providerId)
        // fakeAddress().words is returned by FakeW3WTextDataSource
        assertEquals(fakeAddress().words, result.value.address.words)
    }

    @Test
    fun resolve_withUnknownProviderId_returnsProviderNotResolvable() = runTest {
        val client = buildClient()
        val suggestion = SearchResult.SearchSuggestion(
            query = "test",
            providerId = "unknown_provider",
            title = "",
            extras = mapOf("lat" to "21.0245", "lng" to "105.8412"),
        )

        val result = client.resolve(suggestion)

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
        assertIs<ProviderNotResolvableException>(result.error)
    }

    @Test
    fun resolve_withLanguageConfig_usesConfiguredLanguage() = runTest {
        val config = defaultConfig.copy(language = W3WRFC5646Language.FR_FR)
        val client = buildClient(config = config)
        val suggestion = SearchResult.SearchSuggestion(
            query = "Hanoi",
            providerId = MAPBOX_PROVIDER_ID,
            title = "Hanoi",
            extras = mapOf("lat" to "21.0245", "lng" to "105.8412"),
        )

        // Verify resolve succeeds — language is forwarded to FakeW3WTextDataSource.convertTo3wa.
        val result = client.resolve(suggestion)

        assertIs<W3WResult.Success<SearchResult.ResolvedAddress>>(result)
    }
}
