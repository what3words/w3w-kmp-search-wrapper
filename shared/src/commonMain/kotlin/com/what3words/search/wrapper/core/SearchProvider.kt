package com.what3words.search.wrapper.core

import com.what3words.core.types.common.W3WResult

/**
 * Defines a provider capable of handling specific types of search queries.
 */
interface SearchProvider {
    /** The unique identifier for this search provider. */
    val providerId: String

    /**
     * Determines if this provider can handle the given query.
     *
     * @param query The search query string.
     * @return True if the provider can process this query, false otherwise.
     */
    fun canHandle(query: String): Boolean

    /**
     * Executes the search operation for the given query.
     *
     * @param query The search query string.
     * @return A [W3WResult] containing a list of [SearchResult]s.
     */
    @Throws(Exception::class)
    suspend fun executeSearch(query: String): W3WResult<List<SearchResult>>
}

/**
 * Extends [SearchProvider] with the ability to resolve a [SearchResult.SearchSuggestion]
 * into a fully resolved [SearchResult.ResolvedAddress].
 */
interface ResolvableSearchProvider : SearchProvider {

    /**
     * Resolves a [SearchResult.SearchSuggestion] into a [SearchResult.ResolvedAddress].
     *
     * @param data The suggestion to resolve.
     * @return A [W3WResult] containing the resolved address, or a failure if resolution fails.
     */
    suspend fun resolve(data: SearchResult.SearchSuggestion): W3WResult<SearchResult.ResolvedAddress>
}