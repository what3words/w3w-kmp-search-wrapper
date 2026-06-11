package com.what3words.search.wrapper.threewordaddress.helper

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

/**
 * Normalises a space-separated three-word address into the canonical dot-separated form by treating
 * every space/NBSP as a word boundary and replacing it with a dot. This completes both fully
 * space-separated input ("index home raft" -> "index.home.raft") and partially dotted input
 * ("index.home raft" -> "index.home.raft").
 *
 * This is only applied when allowSpaceSeparator is enabled, which clients must keep disabled for
 * spaced languages such as Vietnamese — there spaces occur inside words rather than between them,
 * so those queries are forwarded verbatim instead and resolve via their dot delimiters.
 */
internal fun String.normalizeSpaceSeparatedQuery(): String =
    trim().replace(SPACE_SEPARATOR_REGEX, ".")
