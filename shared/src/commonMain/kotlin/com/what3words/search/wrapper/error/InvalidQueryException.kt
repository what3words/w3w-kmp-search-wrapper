package com.what3words.search.wrapper.error

import com.what3words.core.types.common.W3WError

/**
 * Thrown when a search query does not match the expected format for a given
 * [com.what3words.search.wrapper.core.SearchProvider].
 */
class InvalidQueryException : W3WError(message = "Query is not a valid three-word-address-like input")
