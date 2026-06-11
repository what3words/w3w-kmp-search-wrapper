package com.what3words.search.wrapper.threewordaddress.helper

import kotlin.math.abs

private const val W3W_WORD = """(?:\p{L}\p{M}*)+"""

// Dot-like punctuation covering Latin, CJK, Arabic, Devanagari, etc.
private const val W3W_DOT_DELIMITER = """[.｡。･・︒។։။۔።।]"""

// Same as above, extended with a regular space and non-breaking space.
private const val W3W_DOT_OR_SPACE_DELIMITER = """[.｡。･・︒។།۔።।\u0020\u00A0]"""

private val W3W_PATTERN = Regex(
    """^/*$W3W_WORD$W3W_DOT_DELIMITER$W3W_WORD$W3W_DOT_DELIMITER$W3W_WORD$""",
    setOf(RegexOption.IGNORE_CASE),
)

private val W3W_PATTERN_ALLOW_SPACES = Regex(
    """^/*$W3W_WORD$W3W_DOT_OR_SPACE_DELIMITER$W3W_WORD$W3W_DOT_OR_SPACE_DELIMITER$W3W_WORD$""",
    setOf(RegexOption.IGNORE_CASE),
)

internal fun String.isA3WordAddress(allowSpaces: Boolean = false): Boolean =
    if (allowSpaces) W3W_PATTERN_ALLOW_SPACES.matches(trim()) else W3W_PATTERN.matches(trim())

private val MAY_BE_W3W_PATTERN = Regex(
    """^/*(?:\p{L}\p{M}*){1,}([.｡。･・︒។։။۔።। ,\\\\^_/+'&\\:;|　-]{1,2})(?:\p{L}\p{M}*){1,}([.｡。･・︒។։။۔።। ,\\\\^_/+'&\\:;|　-]{1,2})(?:\p{L}\p{M}*){1,}$""",
)
private val NON_LETTER_REGEX = "\\P{L}+".toRegex()

/**
 * Normalizes a loose three-word-address-like query by replacing detected separators with dots.
 * Returns null when the query does not look like a possible three-word address.
 */
internal fun String.mayBeA3WordAddress(): String? {
    val lowercaseString = normalizeThreeWordAddressQuery().lowercase()
    val matchResult = MAY_BE_W3W_PATTERN.find(lowercaseString) ?: return null

    var threeWordAddress = lowercaseString
    for (groupIndex in 1..<matchResult.groupValues.size) {
        val separator = matchResult.groupValues[groupIndex]
        if (separator.isNotEmpty()) {
            threeWordAddress = threeWordAddress.replace(separator, ".")
        }
    }

    return threeWordAddress
}

internal fun String.normalizeThreeWordAddressQuery(): String =
    if (startsWith("///") || endsWith("///")) replace("/", "").trim() else this

internal fun String.lettersOnly(): String = replace(NON_LETTER_REGEX, "").lowercase()

private val SPACE_SEPARATOR_REGEX = Regex("""[\u0020\u00A0]""")

/** Replaces space/NBSP separators with dots to produce a canonical dot-separated three-word address. */
internal fun String.normalizeToCanonicalForm(): String = trim().replace(SPACE_SEPARATOR_REGEX, ".")

private val W3W_DOT_DELIMITER_REGEX = Regex(W3W_DOT_DELIMITER)

private const val REQUIRED_WORD_COUNT = 3

// In languages whose words contain spaces (e.g. Vietnamese), a single what3words word rarely
// exceeds three space-separated tokens.
private const val MAX_TOKENS_PER_WORD = 3

/**
 * Generates candidate dot-separated three-word addresses for a loosely delimited query,
 * most-likely first.
 *
 * Dots are hard word boundaries — a query that already contains two of them is returned as a
 * single candidate with its word-internal spaces preserved (e.g. Vietnamese
 * "xôi đậu.đậu tằm.vui vẻ"). With fewer dots, every way of promoting space positions to the
 * missing boundaries is generated, keeping only words of [MAX_TOKENS_PER_WORD] tokens or fewer,
 * and ranked to prefer evenly sized words (spaced-language words are most commonly two tokens).
 * Ties keep earlier boundary positions first.
 *
 * Returns an empty list when the query cannot form exactly three words (too many dots, or not
 * enough tokens), in which case callers should fall back to [normalizeToCanonicalForm].
 */
internal fun String.segmentationCandidates(): List<String> {
    val segments = trim()
        .split(W3W_DOT_DELIMITER_REGEX)
        .map { segment -> segment.split(SPACE_SEPARATOR_REGEX).filter(String::isNotEmpty) }
        .filter { it.isNotEmpty() }

    if (segments.isEmpty() || segments.size > REQUIRED_WORD_COUNT) return emptyList()
    if (segments.size == REQUIRED_WORD_COUNT) {
        return listOf(segments.joinToString(".") { it.joinToString(" ") })
    }

    val tokens = segments.flatten()
    // Gap g sits between tokens[g] and tokens[g + 1]. Gaps at segment edges are fixed by the
    // dots the user typed; the remaining boundaries must be promoted from the soft (space) gaps.
    val hardBoundaries = segments.dropLast(1)
        .runningFold(0) { tokenCount, segment -> tokenCount + segment.size }
        .drop(1)
        .map { it - 1 }
    val softGaps = (0..tokens.size - 2).filter { it !in hardBoundaries }

    return softGaps.combinations(REQUIRED_WORD_COUNT - segments.size)
        .map { promoted -> splitAtBoundaries(tokens, (hardBoundaries + promoted).sorted()) }
        .filter { words -> words.all { it.size <= MAX_TOKENS_PER_WORD } }
        .sortedBy { words -> words.sumOf { abs(it.size - 2) } }
        .map { words -> words.joinToString(".") { it.joinToString(" ") } }
}

private fun splitAtBoundaries(tokens: List<String>, boundaries: List<Int>): List<List<String>> {
    val starts = listOf(0) + boundaries.map { it + 1 }
    val ends = boundaries.map { it + 1 } + listOf(tokens.size)
    return starts.zip(ends) { from, to -> tokens.subList(from, to) }
}

private fun <T> List<T>.combinations(k: Int): List<List<T>> = when {
    k == 0 -> listOf(emptyList())
    k > size -> emptyList()
    else -> flatMapIndexed { index, element ->
        drop(index + 1).combinations(k - 1).map { rest -> listOf(element) + rest }
    }
}
