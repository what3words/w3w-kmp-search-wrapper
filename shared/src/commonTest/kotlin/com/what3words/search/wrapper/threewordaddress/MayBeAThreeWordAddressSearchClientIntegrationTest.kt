package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.domain.W3WSuggestion
import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.search.wrapper.fake.FakeW3WTextDataSource
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_SUGGESTED_ADDRESS
import com.what3words.search.wrapper.core.W3WSearchClient
import com.what3words.search.wrapper.error.ProviderNotFoundException
import com.what3words.search.wrapper.fixtures.fakeAddress
import com.what3words.search.wrapper.fixtures.pluginFor
import com.what3words.search.wrapper.fixtures.suggestion
import com.what3words.search.wrapper.threewordaddress.MayBeAThreeWordAddressSearchConfig
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class MayBeAThreeWordAddressSearchClientIntegrationTest {

    private val defaultDataSource = FakeW3WTextDataSource().apply {
        autosuggestResult = W3WResult.Success(listOf(W3WSuggestion(fakeAddress(), 1, null)))
    }

    private fun buildClient(
        textDataSource: W3WTextDataSource = defaultDataSource,
        mayBeThreeWordAddressPriority: Int = 10,
        fallbackProvider: Pair<String, Int>? = null,
    ): W3WSearchClient {
        return W3WSearchClient(textDataSource) {
            install(MayBeAThreeWordAddressSearch, priority = mayBeThreeWordAddressPriority)
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

    @Test
    fun search_withPrefixThreeWordAddress_returnsSuggestionFromMayBeProvider() = runTest {
        val client = buildClient()

        val result = client.search("///filled.count.soap")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        val suggestion = assertIs<SearchResult.SearchSuggestion>(result.value.first())
        assertEquals(MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID, suggestion.providerId)
    }

    @Test
    fun search_withoutPrefixThreeWordAddress_returnsSuggestionFromMayBeProvider() = runTest {
        val client = buildClient()

        val result = client.search("filled count soap")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        val suggestion = assertIs<SearchResult.SearchSuggestion>(result.value.first())
        assertEquals(MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID, suggestion.providerId)
        assertEquals("///filled.count.soap", suggestion.extras[EXTRAS_KEY_SUGGESTED_ADDRESS])
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
    fun search_withHigherPriorityProvider_handlesMayBeAddressFirst() = runTest {
        val mayBeProvider = MayBeAThreeWordAddressSearchProvider(
            defaultDataSource,
            MayBeAThreeWordAddressSearchConfig(),
        )
        val fallbackProvider = fakeProvider("fallback", results = listOf(suggestion("fallback")))

        val client = W3WSearchClient(defaultDataSource) {
            install(pluginFor(mayBeProvider), priority = 20)
            install(pluginFor(fallbackProvider), priority = 10)
        }

        val result = client.search("filled count soap")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID, result.value.first().providerId)
    }

    @Test
    fun search_withMultipleProvidersInSamePriority_executesAll() = runTest {
        val client = W3WSearchClient(defaultDataSource) {
            install(MayBeAThreeWordAddressSearch, priority = 10)
            install(
                pluginFor(fakeProvider("other", results = listOf(suggestion("other")))),
                priority = 10,
            )
        }

        val result = client.search("filled count soap")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertTrue(result.value.any { it.providerId == MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID })
    }

    @Test
    fun search_withCustomLanguage_usesConfiguredValue() = runTest {
        val trackingDataSource = FakeW3WTextDataSource().apply {
            autosuggestResult = W3WResult.Success(listOf(W3WSuggestion(fakeAddress(), 1, null)))
        }
        val client = W3WSearchClient(trackingDataSource) {
            install(MayBeAThreeWordAddressSearch, priority = 10) {
                fallbackLanguage = W3WRFC5646Language.FR_FR
            }
        }

        client.search("filled count soap")

        assertEquals(W3WRFC5646Language.FR_FR, trackingDataSource.lastAutosuggestOptions?.language)
    }
}
