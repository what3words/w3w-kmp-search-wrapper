package com.what3words.search.wrapper.bng

import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.W3WSearchClient
import com.what3words.search.wrapper.error.ProviderNotFoundException
import com.what3words.search.wrapper.fake.FakeW3WTextDataSource
import com.what3words.search.wrapper.fake.fakeProvider
import com.what3words.search.wrapper.fixtures.pluginFor
import com.what3words.search.wrapper.fixtures.suggestion
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class BritishNationalGridSearchClientIntegrationTest {

    @Test
    fun search_withOSGridReference_resolvesAddress() = runTest {
        val mockDataSource = FakeW3WTextDataSource()
        val client = W3WSearchClient(mockDataSource) {
            install(BritishNationalGridSearch, priority = 10) {
                language = W3WRFC5646Language.EN_GB
            }
        }

        val result = client.search("TQ388797")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        val searchResult = result.value.first()
        assertIs<SearchResult.ResolvedAddress>(searchResult)
        assertEquals(BRITISH_NATIONAL_GRID_PROVIDER_ID, searchResult.providerId)
        assertEquals("filled.count.soap", searchResult.address.words)
    }

    @Test
    fun search_withEastingNorthing_resolvesAddress() = runTest {
        val mockDataSource = FakeW3WTextDataSource()
        val client = W3WSearchClient(mockDataSource) {
            install(BritishNationalGridSearch, priority = 10)
        }

        val result = client.search("538800, 179700")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        val searchResult = result.value.first()
        assertIs<SearchResult.ResolvedAddress>(searchResult)
        assertEquals(BRITISH_NATIONAL_GRID_PROVIDER_ID, searchResult.providerId)
    }

    @Test
    fun search_withNonBngQuery_skipsAndUsesFallback() = runTest {
        val mockDataSource = FakeW3WTextDataSource()
        val client = W3WSearchClient(mockDataSource) {
            install(BritishNationalGridSearch, priority = 10)
            install(
                pluginFor(fakeProvider("fallback", results = listOf(suggestion("fallback")))),
                priority = 1
            )
        }

        val result = client.search("filled.count.soap")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        assertEquals("fallback", result.value.first().providerId)
    }

    @Test
    fun search_handlesApiFailureGracefully() = runTest {
        val mockDataSource = FakeW3WTextDataSource().apply {
            convertTo3waResult = W3WResult.Failure(ProviderNotFoundException())
        }
        val client = W3WSearchClient(mockDataSource) {
            install(BritishNationalGridSearch, priority = 10)
        }

        val result = client.search("TQ388797")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        assertIs<ProviderNotFoundException>(result.error)
    }
}
