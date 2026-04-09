package com.what3words.search.wrapper.maybethreewordaddress.helper

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
