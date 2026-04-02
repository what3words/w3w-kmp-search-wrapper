package com.what3words.search.wrapper.core

import com.what3words.core.types.domain.W3WAddress

/**
 * Represents a generic search result from a unified search provider.
 *
 * @property query The original search query string.
 * @property providerId The identifier of the provider that returned this result.
 */
sealed class SearchResult(open val query: String, open val providerId: String) {

    /**
     * A search result that has been fully resolved to a [W3WAddress].
     *
     * @property address The resolved what3words address.
     */
    data class ResolvedAddress(
        override val query: String,
        override val providerId: String,
        val address: W3WAddress
    ) : SearchResult(query, providerId)

    /**
     * A search result that requires further resolution to obtain a [W3WAddress].
     * Typically displayed as a suggestion to the user before selection.
     *
     * Use [ResolvableSearchProvider.resolve] to convert this into a [ResolvedAddress].
     *
     * @property title Primary display text for the suggestion (e.g. street name or place name).
     * @property subtitle Optional secondary display text (e.g. city or region). `null` when absent.
     * @property extras Provider-specific metadata associated with this suggestion.
     */
    data class SearchSuggestion(
        override val query: String,
        override val providerId: String,
        val title: String,
        val subtitle: String? = null,
        val extras: Map<String, String>,
    ) : SearchResult(query, providerId)
}