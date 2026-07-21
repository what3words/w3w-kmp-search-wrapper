package com.what3words.search.wrapper.error

import com.what3words.core.types.common.W3WError

/**
 * Thrown when a [com.what3words.search.wrapper.core.SearchResult.SearchSuggestion] is resolved
 * but its extras map does not contain the required title key.
 */
class MissingSuggestionTitleException : W3WError(message = "Missing title in suggestion extras")
