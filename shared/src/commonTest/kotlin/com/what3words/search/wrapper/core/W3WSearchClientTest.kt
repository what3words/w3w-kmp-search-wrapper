package com.what3words.search.wrapper.core

import com.what3words.core.types.common.W3WResult
import com.what3words.search.wrapper.error.ProviderNotFoundException
import com.what3words.search.wrapper.error.ProviderNotResolvableException
import com.what3words.search.wrapper.fake.failingProvider
import com.what3words.search.wrapper.fake.fakeProvider
import com.what3words.search.wrapper.fake.fakeResolvableProvider
import com.what3words.search.wrapper.fixtures.buildClient
import com.what3words.search.wrapper.fixtures.fakeAddress
import com.what3words.search.wrapper.fixtures.pluginFor
import com.what3words.search.wrapper.fixtures.suggestion
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class W3WSearchClientTest {

    // --- search(): priority dispatch ---

    @Test
    fun search_usesHighestPriorityTierFirst() = runTest {
        val client = buildClient {
            install(
                pluginFor(fakeProvider("low", results = listOf(suggestion("low")))),
                priority = 1
            )
            install(
                pluginFor(fakeProvider("high", results = listOf(suggestion("high")))),
                priority = 10
            )
        }

        val result = client.search("test")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        assertEquals("high", result.value.first().providerId)
    }

    @Test
    fun search_skipsLowerPriorityTierWhenHigherTierReturnsResults() = runTest {
        val client = buildClient {
            install(
                pluginFor(fakeProvider("high", results = listOf(suggestion("high")))),
                priority = 10
            )
            install(
                pluginFor(fakeProvider("low", results = listOf(suggestion("low")))),
                priority = 1
            )
        }

        val result = client.search("test")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        val providerIds = result.value.map { it.providerId }
        assertTrue("high" in providerIds)
        assertFalse("low" in providerIds)
    }

    @Test
    fun search_doNotFallThroughToNextTierWhenHigherTierReturnsEmpty() = runTest {
        val client = buildClient {
            install(pluginFor(fakeProvider("high", results = emptyList())), priority = 10)
            install(
                pluginFor(fakeProvider("low", results = listOf(suggestion("low")))),
                priority = 1
            )
        }

        val result = client.search("test")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(emptyList(), result.value)
    }

    // --- search(): same-priority parallel execution ---

    @Test
    fun search_mergesResultsFromSamePriorityProviders() = runTest {
        val client = buildClient {
            install(pluginFor(fakeProvider("a", results = listOf(suggestion("a")))), priority = 5)
            install(pluginFor(fakeProvider("b", results = listOf(suggestion("b")))), priority = 5)
        }

        val result = client.search("test")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(2, result.value.size)
        assertEquals(setOf("a", "b"), result.value.map { it.providerId }.toSet())
    }

    @Test
    fun search_returnsPartialResultsWhenOneProviderInTierFails() = runTest {
        val client = buildClient {
            install(pluginFor(failingProvider("fail")), priority = 5)
            install(pluginFor(fakeProvider("ok", results = listOf(suggestion("ok")))), priority = 5)
        }

        val result = client.search("test")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        assertEquals("ok", result.value.first().providerId)
    }

    @Test
    fun search_returnsFailureWhenAllProvidersInTierFail() = runTest {
        val client = buildClient {
            install(pluginFor(failingProvider("fail1")), priority = 5)
            install(pluginFor(failingProvider("fail2")), priority = 5)
        }

        val result = client.search("test")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        assertIs<ProviderNotFoundException>(result.error)
    }

    @Test
    fun search_doesNotFallThroughToNextTierWhenHigherTierProvidersAllFail() = runTest {
        // Higher tier has capable providers that all fail at runtime — the lower tier should
        // never be reached, because canHandle is the only criterion for tier fallback.
        val client = buildClient {
            install(pluginFor(failingProvider("high-fail")), priority = 10)
            install(
                pluginFor(fakeProvider("low", results = listOf(suggestion("low")))),
                priority = 1
            )
        }

        val result = client.search("test")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        assertIs<ProviderNotFoundException>(result.error)
    }

    // --- search(): canHandle filtering ---

    @Test
    fun search_skipsProviderThatCannotHandleQuery() = runTest {
        val client = buildClient {
            install(
                pluginFor(
                    fakeProvider(
                        "skip",
                        handles = false,
                        results = listOf(suggestion("skip"))
                    )
                ), priority = 10
            )
            install(
                pluginFor(fakeProvider("capable", results = listOf(suggestion("capable")))),
                priority = 1
            )
        }

        val result = client.search("test")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals("capable", result.value.first().providerId)
    }

    // --- search(): failure cases ---

    @Test
    fun search_returnsProviderNotFoundWhenNoProviderCanHandleQuery() = runTest {
        val client = buildClient {
            install(pluginFor(fakeProvider("a", handles = false)), priority = 5)
        }

        val result = client.search("test")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        assertIs<ProviderNotFoundException>(result.error)
    }

    // --- search(): backward compatibility ---

    @Test
    fun search_withDefaultPriorityAllProvidersRunInSameTier() = runTest {
        val client = buildClient {
            install(
                pluginFor(fakeProvider("first", results = listOf(suggestion("first")))),
                priority = 0
            )
            install(
                pluginFor(fakeProvider("second", results = listOf(suggestion("second")))),
                priority = 0
            )
        }

        // Both at priority=0 → same tier → both results merged
        val result = client.search("test")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(2, result.value.size)
        assertEquals(setOf("first", "second"), result.value.map { it.providerId }.toSet())
    }

    // --- resolve() ---

    @Test
    fun resolve_routesToCorrectProviderByProviderId() = runTest {
        val address = fakeAddress()
        val client = buildClient {
            install(
                pluginFor(fakeResolvableProvider("target", resolvedAddress = address)),
                priority = 0
            )
            install(pluginFor(fakeResolvableProvider("other")), priority = 0)
        }

        val result = client.resolve(suggestion("target"))

        assertIs<W3WResult.Success<SearchResult.ResolvedAddress>>(result)
        assertEquals("target", result.value.providerId)
        assertEquals(address, result.value.address)
    }

    @Test
    fun resolve_returnsProviderNotResolvableForNonResolvableProvider() = runTest {
        val client = buildClient {
            install(pluginFor(fakeProvider("nonresolvable")), priority = 0)
        }

        val result = client.resolve(suggestion("nonresolvable"))

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
        assertIs<ProviderNotResolvableException>(result.error)
    }

    @Test
    fun resolve_returnsProviderNotResolvableWhenProviderNotFound() = runTest {
        val client = buildClient {
            install(pluginFor(fakeProvider("known")), priority = 0)
        }

        val result = client.resolve(suggestion("unknown"))

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
        assertIs<ProviderNotResolvableException>(result.error)
    }
}
