package com.what3words.search.wrapper.error

import com.what3words.core.types.common.W3WError

/**
 * Thrown when no [com.what3words.search.wrapper.core.SearchProvider] is found that can handle
 * the given search query across all registered priority tiers.
 */
class ProviderNotFoundException: W3WError(message = "Provider not found")

/**
 * Thrown when the [com.what3words.search.wrapper.core.SearchProvider] that originated a
 * [com.what3words.search.wrapper.core.SearchResult.SearchSuggestion] does not implement
 * [com.what3words.search.wrapper.core.ResolvableSearchProvider] or cannot be found.
 */
class ProviderNotResolvableException: W3WError(message = "Provider not resolvable")