package com.what3words.search.wrapper.fake

import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.domain.W3WAddress
import com.what3words.search.wrapper.core.ResolvableSearchProvider
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.error.ProviderNotFoundException

/**
 * Creates a [SearchProvider] that returns the given [results] and optionally delegates
 * [SearchProvider.canHandle] based on [handles].
 */
internal fun fakeProvider(
    id: String,
    handles: Boolean = true,
    results: List<SearchResult> = emptyList(),
): SearchProvider = object : SearchProvider {
    override val providerId = id
    override fun canHandle(query: String) = handles
    override suspend fun executeSearch(query: String) = W3WResult.Success(results)
}

/**
 * Creates a [SearchProvider] that always fails with [ProviderNotFoundException].
 */
internal fun failingProvider(id: String): SearchProvider = object : SearchProvider {
    override val providerId = id
    override fun canHandle(query: String) = true
    override suspend fun executeSearch(query: String): W3WResult<List<SearchResult>> =
        W3WResult.Failure(ProviderNotFoundException())
}

/**
 * Creates a [ResolvableSearchProvider] that resolves suggestions using [resolvedAddress],
 * or fails with [ProviderNotFoundException] if [resolvedAddress] is null.
 */
internal fun fakeResolvableProvider(
    id: String,
    handles: Boolean = true,
    results: List<SearchResult> = emptyList(),
    resolvedAddress: W3WAddress? = null,
): ResolvableSearchProvider = object : ResolvableSearchProvider {
    override val providerId = id
    override fun canHandle(query: String) = handles
    override suspend fun executeSearch(query: String) = W3WResult.Success(results)
    override suspend fun resolve(data: SearchResult.SearchSuggestion): W3WResult<SearchResult.ResolvedAddress> =
        resolvedAddress
            ?.let { W3WResult.Success(SearchResult.ResolvedAddress(data.query, id, it)) }
            ?: W3WResult.Failure(ProviderNotFoundException())
}
