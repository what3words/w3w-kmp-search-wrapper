package com.what3words.search.wrapper.coordinates

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

class CoordinatesSearchClientIntegrationTest {

    @Test
    fun search_withCoordinatesSearch_resolvesValidCoordinates() = runTest {
        val mockDataSource = FakeW3WTextDataSource()
        val client = W3WSearchClient(mockDataSource) {
            install(CoordinatesSearch, priority = 10) {
                language = W3WRFC5646Language.EN_GB
            }
        }

        val result = client.search("51.520847, -0.195521")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        val searchResult = result.value.first()
        assertIs<SearchResult.ResolvedAddress>(searchResult)
        assertEquals("CoordinatesSearchProvider", searchResult.providerId)
        assertEquals("filled.count.soap", searchResult.address.words)
    }

    @Test
    fun search_withCoordinatesSearch_skipsAndUsesFallbackWhenQueryIsNotCoordinates() = runTest {
        val mockDataSource = FakeW3WTextDataSource()
        val client = W3WSearchClient(mockDataSource) {
            install(CoordinatesSearch, priority = 10)
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
    fun search_withCoordinatesSearch_handlesApiFailureGracefully() = runTest {
        val mockDataSource = FakeW3WTextDataSource().apply {
            convertTo3waResult = W3WResult.Failure(ProviderNotFoundException())
        }
        val client = W3WSearchClient(mockDataSource) {
            install(CoordinatesSearch, priority = 10)
        }

        val result = client.search("51.520847, -0.195521")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        assertIs<ProviderNotFoundException>(result.error)
    }
}
