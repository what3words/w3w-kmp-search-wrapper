package com.what3words.search.wrapper.core.util

/**
 * Check if a string contains any Chinese, Japanese or Korean characters.
 */
internal fun String.hasCJKChars(): Boolean {
    return this.trim().any { char ->
        val code = char.code
        // CJK Unified Ideographs
        (code in 0x4E00..0x9FFF) ||
        // CJK Compatibility Ideographs
        (code in 0xF900..0xFAFF) ||
        // CJK Unified Ideographs Extension A
        (code in 0x3400..0x4DBF)
    }
}

/**
 * Returns `true` if this string is a valid search query for the given [minLength].
 *
 * A query shorter than [minLength] (after trimming) is still considered valid if it contains
 * CJK (Chinese, Japanese, or Korean) characters, which are meaningful at shorter lengths.
 *
 * @param minLength Minimum trimmed length required for non-CJK queries.
 */
internal fun String.isValidSearchQuery(minLength: Int) =
    if (this.trim().length < minLength) this.hasCJKChars() else true
