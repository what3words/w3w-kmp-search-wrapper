package com.what3words.search.wrapper.bng.helper

import com.what3words.core.types.geometry.W3WCoordinates
import kotlin.math.pow

/**
 * Holds a raw easting and northing value pair parsed from a coordinate string.
 *
 * @property easting Easting value in metres.
 * @property northing Northing value in metres.
 */
data class EastingNorthing(val easting: Int, val northing: Int)

/**
 * Utility for recognising and converting UK National Grid references to WGS84 coordinates.
 *
 * Supports two input formats:
 * - Two-letter OS grid references followed by 6, 8, or 10 digits (e.g. `TQ388797`).
 * - Comma-separated easting/northing pairs (e.g. `538800, 179700`).
 */
internal object UKNationalGridTransformer {

    private fun isOSGridTG(value: String): Boolean {
        return Regex("^[HSNOT][A-Z](\\d{6}|\\d{8}|\\d{10})$").matches(value)
    }

    private fun parseEastingNorthing(value: String): EastingNorthing? {
        val comps = value.split(",").map { it.trim() }
        if (comps.size != 2) {
            return null
        }

        return try {
            EastingNorthing(comps[0].toInt(), comps[1].toInt())
        } catch (_: NumberFormatException) {
            null
        }
    }

    /**
     * Returns `true` if [value] is a valid comma-separated easting/northing pair within the
     * bounds of the British National Grid (easting 1–700000, northing 1–1300000).
     */
    internal fun isEastingNorthing(value: String): Boolean {
        val str = value.replace(" ", "")
        val comps = str.split(",")
        if (comps.size != 2) return false

        try {
            val easting = comps[0].toInt()
            val northing = comps[1].toInt()
            return easting in 1..700000 && northing in 1..1300000
        } catch (_: NumberFormatException) {
            return false
        }
    }

    /**
     * Returns `true` if [value] is any recognised British National Grid format — either a
     * two-letter OS grid reference string or a valid easting/northing pair.
     */
    internal fun isOSGrid(value: String): Boolean {
        return isOSGridTG(value) || isEastingNorthing(value)
    }

    private fun getOSRefFromSixFigureReference(ref: String): OSRef {
        val char1 = ref[0]
        val char2 = ref[1]
        val precision = ref.length - 2
        val x = precision / 2
        val y = 10.0.pow((10.0 - precision) / 2.0).toInt()
        var east = ref.substring(2, x + 2).toInt() * y
        var north = ref.substring(x + 2, precision + 2).toInt() * y

        when (char1) {
            'H' -> north += 1000000
            'N' -> north += 500000
            'O' -> {
                north += 500000
                east += 500000
            }

            'T' -> east += 500000
        }

        var char2ord = char2.code
        if (char2ord > 73) {
            char2ord-- // Adjust for no I (ASCII 73)
        }
        val nx = ((char2ord - 65) % 5) * 100000
        val ny = (4 - ((char2ord - 65) / 5)) * 100000
        return OSRef(east.toDouble() + nx, north.toDouble() + ny)
    }

    /**
     * Converts a British National Grid [value] to WGS84 [W3WCoordinates].
     *
     * Accepts both two-letter OS grid references and comma-separated easting/northing pairs.
     *
     * @return The corresponding [W3WCoordinates] in WGS84, or `null` if [value] cannot be parsed.
     */
    internal fun getCoordinatesFromOSGrid(value: String): W3WCoordinates? {
        if (isOSGridTG(value)) {
            val os = getOSRefFromSixFigureReference(value)
            val latlng = os.toCoordinates()
            return latlng.osgb36ToWgs84()
        } else if (isEastingNorthing(value)) {
            parseEastingNorthing(value)?.also { en ->
                val os = OSRef(en.easting.toDouble(), en.northing.toDouble())
                val latlng = os.toCoordinates()
                return latlng.osgb36ToWgs84()
            }
        }
        return null
    }
}
