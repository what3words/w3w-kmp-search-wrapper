package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.domain.W3WSuggestion
import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.core.types.geometry.W3WGridSection
import com.what3words.core.types.geometry.W3WRectangle
import com.what3words.core.types.language.W3WLanguage
import com.what3words.core.types.language.W3WProprietaryLanguage
import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.core.types.options.W3WAutosuggestOptions
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.W3WSearchClient
import com.what3words.search.wrapper.error.ProviderNotFoundException
import com.what3words.search.wrapper.fixtures.fakeAddress
import com.what3words.search.wrapper.fixtures.pluginFor
import com.what3words.search.wrapper.fixtures.suggestion
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ThreeWordAddressSearchClientIntegrationTest {

    private val defaultDataSource = object : W3WTextDataSource {
        override fun version(version: W3WTextDataSource.Version): String? = null
        override fun convertTo3wa(coordinates: W3WCoordinates, language: W3WLanguage) =
            W3WResult.Success(fakeAddress())

        override fun convertToCoordinates(words: String) = W3WResult.Success(fakeAddress())
        override fun autosuggest(input: String, options: W3WAutosuggestOptions?) =
            W3WResult.Success(listOf(W3WSuggestion(fakeAddress(), 1, null)))

        override fun gridSection(boundingBox: W3WRectangle): W3WResult<W3WGridSection> =
            throw NotImplementedError()

        override fun availableLanguages(): W3WResult<Set<W3WProprietaryLanguage>> =
            throw NotImplementedError()

        override fun isValid3wa(words: String): W3WResult<Boolean> =
            throw NotImplementedError()
    }

    private fun buildClient(
        textDataSource: W3WTextDataSource = defaultDataSource,
        threeWordAddressPriority: Int = 10,
        fallbackProvider: Pair<String, Int>? = null,
    ): W3WSearchClient {
        return W3WSearchClient(textDataSource) {
            install(ThreeWordAddressSearch, priority = threeWordAddressPriority)
            fallbackProvider?.let { (id, priority) ->
                install(
                    pluginFor(fakeProvider(id, results = listOf(suggestion(id)))),
                    priority = priority,
                )
            }
        }
    }

    private fun fakeProvider(
        id: String,
        handles: Boolean = true,
        results: List<SearchResult> = emptyList(),
    ) = object : SearchProvider {
        override val providerId = id
        override fun canHandle(query: String) = handles
        override suspend fun executeSearch(query: String) = W3WResult.Success(results)
    }

    // ── search ────────────────────────────────────────────────────────────────

    @Test
    fun search_withThreeWordAddress_returnsSuggestionsFromThreeWordAddressProvider() = runTest {
        val client = buildClient()

        val result = client.search("filled.count.soap")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        val address = assertIs<SearchResult.ResolvedAddress>(result.value.first())
        assertEquals(THREE_WORD_ADDRESS_PROVIDER_ID, address.providerId)
    }

    @Test
    fun search_withNormalAddressQuery_fallsThrough_toFallbackProvider() = runTest {
        val client = buildClient(fallbackProvider = "fallback" to 1)

        val result = client.search("123 Main Street")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        assertEquals("fallback", result.value.first().providerId)
    }

    @Test
    fun search_withNormalAddressQuery_andNoFallback_returnsProviderNotFound() = runTest {
        val client = buildClient()

        val result = client.search("123 Main Street")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        assertIs<ProviderNotFoundException>(result.error)
    }

    @Test
    fun search_withShortQuery_fallsThrough_toFallbackProvider() = runTest {
        val client = buildClient(fallbackProvider = "fallback" to 1)

        val result = client.search("ab")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        assertEquals("fallback", result.value.first().providerId)
    }

    @Test
    fun search_withBlankQuery_returnsProviderNotFound() = runTest {
        val client = buildClient()

        val result = client.search("")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        assertIs<ProviderNotFoundException>(result.error)
    }

    // ── priority handling ─────────────────────────────────────────────────────

    @Test
    fun search_withHigherPriorityProvider_handlesThreeWordAddressFirst() = runTest {
        val threeWordAddressProvider = ThreeWordAddressSearchProvider(defaultDataSource, ThreeWordAddressSearchConfig())
        val fallbackProvider = fakeProvider("fallback", results = listOf(suggestion("fallback")))

        val client = W3WSearchClient(defaultDataSource) {
            install(pluginFor(threeWordAddressProvider), priority = 20)
            install(pluginFor(fallbackProvider), priority = 10)
        }

        val result = client.search("filled.count.soap")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(THREE_WORD_ADDRESS_PROVIDER_ID, result.value.first().providerId)
    }

    @Test
    fun search_withMultipleProvidersInSamePriority_executesAll() = runTest {
        val client = W3WSearchClient(defaultDataSource) {
            install(ThreeWordAddressSearch, priority = 10)
            install(
                pluginFor(fakeProvider("other", results = listOf(suggestion("other")))),
                priority = 10,
            )
        }

        val result = client.search("filled.count.soap")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertTrue(result.value.any { it.providerId == THREE_WORD_ADDRESS_PROVIDER_ID })
    }

    // ── config options ─────────────────────────────────────────────────────────

    @Test
    fun search_withCustomMaxResults_usesConfiguredValue() = runTest {
        var capturedOptions: W3WAutosuggestOptions? = null
        val trackingDataSource = object : W3WTextDataSource {
            override fun version(version: W3WTextDataSource.Version): String? = null
            override fun convertTo3wa(coordinates: W3WCoordinates, language: W3WLanguage) =
                W3WResult.Success(fakeAddress())

            override fun convertToCoordinates(words: String) = W3WResult.Success(fakeAddress())
            override fun autosuggest(input: String, options: W3WAutosuggestOptions?): W3WResult<List<W3WSuggestion>> {
                capturedOptions = options
                return W3WResult.Success(listOf(W3WSuggestion(fakeAddress(), 1, null)))
            }

            override fun gridSection(boundingBox: W3WRectangle): W3WResult<W3WGridSection> =
                throw NotImplementedError()

            override fun availableLanguages(): W3WResult<Set<W3WProprietaryLanguage>> =
                throw NotImplementedError()

            override fun isValid3wa(words: String): W3WResult<Boolean> =
                throw NotImplementedError()
        }
        val client = W3WSearchClient(trackingDataSource) {
            install(ThreeWordAddressSearch, priority = 10) {
                maxResults = 7
            }
        }

        client.search("filled.count.soap")

        assertTrue(capturedOptions != null && capturedOptions!!.nResults == 7)
    }

    @Test
    fun search_withCustomLanguage_usesConfiguredValue() = runTest {
        var capturedOptions: W3WAutosuggestOptions? = null
        val trackingDataSource = object : W3WTextDataSource {
            override fun version(version: W3WTextDataSource.Version): String? = null
            override fun convertTo3wa(coordinates: W3WCoordinates, language: W3WLanguage) =
                W3WResult.Success(fakeAddress())

            override fun convertToCoordinates(words: String) = W3WResult.Success(fakeAddress())
            override fun autosuggest(input: String, options: W3WAutosuggestOptions?): W3WResult<List<W3WSuggestion>> {
                capturedOptions = options
                return W3WResult.Success(listOf(W3WSuggestion(fakeAddress(), 1, null)))
            }

            override fun gridSection(boundingBox: W3WRectangle): W3WResult<W3WGridSection> =
                throw NotImplementedError()

            override fun availableLanguages(): W3WResult<Set<W3WProprietaryLanguage>> =
                throw NotImplementedError()

            override fun isValid3wa(words: String): W3WResult<Boolean> =
                throw NotImplementedError()
        }
        val client = W3WSearchClient(trackingDataSource) {
            install(ThreeWordAddressSearch, priority = 10) {
                language = W3WRFC5646Language.FR_FR
            }
        }

        client.search("filled.count.soap")

        assertTrue(capturedOptions != null && capturedOptions!!.language == W3WRFC5646Language.FR_FR)
    }
}
