package com.what3words.search.wrapper.threewordaddress.helper

private val W3W_PATTERN = Regex(
    """^/*(?:(?:\p{L}\p{M}*){1,}[.｡。･・︒។։။۔።।](?:\p{L}\p{M}*){1,}[.｡。･・︒។։။۔።।](?:\p{L}\p{M}*){1,}|(?:\p{L}\p{M}*){1,}([\u0020\u00A0](?:\p{L}\p{M}*){1,}){1,3}[.｡。･・︒។։။۔።।](?:\p{L}\p{M}*){1,}([\u0020\u00A0](?:\p{L}\p{M}*){1,}){1,3}[.｡。･・︒។։။۔።।](?:\p{L}\p{M}*){1,}([\u0020\u00A0](?:\p{L}\p{M}*){1,}){1,3})$""",
    setOf(RegexOption.IGNORE_CASE),
)

internal fun String.isA3WordAddress(): Boolean = W3W_PATTERN.matches(trim())
