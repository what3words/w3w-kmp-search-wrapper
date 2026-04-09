package com.what3words.search.wrapper.coordinates.helper

import com.what3words.core.types.geometry.W3WCoordinates

private val DD_PATTERN = Regex(
    """^(-?\d+(\.\d+)?),\s*(-?\d+(\.\d+)?)$""",
    RegexOption.IGNORE_CASE
)
private val DD_PATTERN_PREFIX = Regex(
    """^([SNsn])\s*(\d+\.?\d*)[\s,]*([ewEW])\s*(\d+\.?\d*)$""",
    RegexOption.IGNORE_CASE
)
private val DD_PATTERN_SUFFIX = Regex(
    """^(\d+\.?\d*)\s*([SNsn])[\s,]*(\d+\.?\d*)\s*([ewEW])$""",
    RegexOption.IGNORE_CASE
)
private val DDM_PATTERN = Regex(
    """^(\d{1,3})°\s*(-?\d+\.?\d*)'\s*([SNsn])[\s,]*(\d{1,3})°\s*(-?\d+\.?\d*)'\s*([ewEW])$""",
    RegexOption.IGNORE_CASE
)
private val DMS_PATTERN = Regex(
    """^(\d{1,3})°\s*(\d{1,2})['']\s*([\d.]+)${"\""}\s*([SNsn])[\s,]*(\d{1,3})°\s*(\d{1,2})['']\s*([\d.]+)${"\""}\s*([ewEW])$""",
    RegexOption.IGNORE_CASE
)

/** Returns `true` if the latitude is within the valid WGS-84 range (−90 to 90). */
private fun W3WCoordinates.isValidRange(): Boolean =
    lat in -90.0..90.0

/** Returns `true` if this string matches plain Decimal Degrees format, e.g. `51.5074, -0.1278`. */
internal fun String.isDdPattern(): Boolean = DD_PATTERN.matches(this.trim())

/** Returns `true` if this string matches DD with a cardinal-direction prefix, e.g. `N 51.5074, W 0.1278`. */
internal fun String.isDdPrefixPattern(): Boolean = DD_PATTERN_PREFIX.matches(this.trim())

/** Returns `true` if this string matches DD with a cardinal-direction suffix, e.g. `51.5074 N, 0.1278 W`. */
internal fun String.isDdSuffixPattern(): Boolean = DD_PATTERN_SUFFIX.matches(this.trim())

/** Returns `true` if this string matches Degrees Decimal Minutes format, e.g. `51°30.444'N, 0°7.668'W`. */
internal fun String.isDdmPattern(): Boolean = DDM_PATTERN.matches(this.trim())

/** Returns `true` if this string matches Degrees Minutes Seconds format, e.g. `51°30'26.64"N, 0°7'40.08"W`. */
internal fun String.isDmsPattern(): Boolean = DMS_PATTERN.matches(this.trim())

/**
 * Parses a plain Decimal Degrees string into [W3WCoordinates].
 *
 * @param s Input string matching [DD_PATTERN].
 * @return Parsed coordinates, or `null` if the string is invalid or out of range.
 */
internal fun parseDdCoordinates(s: String): W3WCoordinates? {
    val match = DD_PATTERN.find(s.trim()) ?: return null
    val lat = match.groupValues[1].toDoubleOrNull() ?: return null
    val lng = match.groupValues[3].toDoubleOrNull() ?: return null
    return W3WCoordinates(lat, lng).takeIf { it.isValidRange() }
}

/**
 * Parses a DD string with cardinal-direction prefix into [W3WCoordinates], e.g. `N 51.5074, W 0.1278`.
 *
 * @param s Input string matching [DD_PATTERN_PREFIX].
 * @return Parsed coordinates, or `null` if the string is invalid or out of range.
 */
internal fun parseDdPrefixCoordinates(s: String): W3WCoordinates? {
    val match = DD_PATTERN_PREFIX.find(s.trim()) ?: return null
    val latDir = match.groupValues[1].uppercase()
    val lat = match.groupValues[2].toDoubleOrNull() ?: return null
    val lngDir = match.groupValues[3].uppercase()
    val lng = match.groupValues[4].toDoubleOrNull() ?: return null
    return W3WCoordinates(
        if (latDir == "S") -lat else lat,
        if (lngDir == "W") -lng else lng
    ).takeIf { it.isValidRange() }
}

/**
 * Parses a DD string with cardinal-direction suffix into [W3WCoordinates], e.g. `51.5074 N, 0.1278 W`.
 *
 * @param s Input string matching [DD_PATTERN_SUFFIX].
 * @return Parsed coordinates, or `null` if the string is invalid or out of range.
 */
internal fun parseDdSuffixCoordinates(s: String): W3WCoordinates? {
    val match = DD_PATTERN_SUFFIX.find(s.trim()) ?: return null
    val lat = match.groupValues[1].toDoubleOrNull() ?: return null
    val latDir = match.groupValues[2].uppercase()
    val lng = match.groupValues[3].toDoubleOrNull() ?: return null
    val lngDir = match.groupValues[4].uppercase()
    return W3WCoordinates(
        if (latDir == "S") -lat else lat,
        if (lngDir == "W") -lng else lng
    ).takeIf { it.isValidRange() }
}

/**
 * Parses a Degrees Decimal Minutes string into [W3WCoordinates], e.g. `51°30.444'N, 0°7.668'W`.
 *
 * @param s Input string matching [DDM_PATTERN].
 * @return Parsed coordinates, or `null` if the string is invalid or out of range.
 */
internal fun parseDdmCoordinates(s: String): W3WCoordinates? {
    val match = DDM_PATTERN.find(s.trim()) ?: return null
    val latDeg = match.groupValues[1].toDoubleOrNull() ?: return null
    val latMin = match.groupValues[2].toDoubleOrNull() ?: return null
    val latDir = match.groupValues[3].uppercase()
    val lngDeg = match.groupValues[4].toDoubleOrNull() ?: return null
    val lngMin = match.groupValues[5].toDoubleOrNull() ?: return null
    val lngDir = match.groupValues[6].uppercase()
    val lat = latDeg + latMin / 60.0
    val lng = lngDeg + lngMin / 60.0
    return W3WCoordinates(
        if (latDir == "S") -lat else lat,
        if (lngDir == "W") -lng else lng
    ).takeIf { it.isValidRange() }
}

/**
 * Parses a Degrees Minutes Seconds string into [W3WCoordinates], e.g. `51°30'26.64"N, 0°7'40.08"W`.
 *
 * @param s Input string matching [DMS_PATTERN].
 * @return Parsed coordinates, or `null` if the string is invalid or out of range.
 */
internal fun parseDmsCoordinates(s: String): W3WCoordinates? {
    val match = DMS_PATTERN.find(s.trim()) ?: return null
    val latDeg = match.groupValues[1].toDoubleOrNull() ?: return null
    val latMin = match.groupValues[2].toDoubleOrNull() ?: return null
    val latSec = match.groupValues[3].toDoubleOrNull() ?: return null
    val latDir = match.groupValues[4].uppercase()
    val lngDeg = match.groupValues[5].toDoubleOrNull() ?: return null
    val lngMin = match.groupValues[6].toDoubleOrNull() ?: return null
    val lngSec = match.groupValues[7].toDoubleOrNull() ?: return null
    val lngDir = match.groupValues[8].uppercase()
    val lat = latDeg + latMin / 60.0 + latSec / 3600.0
    val lng = lngDeg + lngMin / 60.0 + lngSec / 3600.0
    return W3WCoordinates(
        if (latDir == "S") -lat else lat,
        if (lngDir == "W") -lng else lng
    ).takeIf { it.isValidRange() }
}
