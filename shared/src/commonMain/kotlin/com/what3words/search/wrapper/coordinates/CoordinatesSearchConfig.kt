package com.what3words.search.wrapper.coordinates

import com.what3words.core.types.language.W3WLanguage
import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.search.wrapper.core.SearchConfig

/**
 * Configuration for [CoordinatesSearchProvider].
 *
 * Controls which coordinate input formats are accepted and the language used
 * when resolving a what3words address from parsed coordinates.
 */
class CoordinatesSearchConfig(
    /** Whether Decimal Degrees (DD) format is enabled, e.g. `51.5074, -0.1278`. */
    var enableDecimal: Boolean = true,

    /** Whether Degrees Decimal Minutes (DDM) format is enabled, e.g. `51°30.444'N, 0°7.668'W`. */
    var enableDDM: Boolean = true,

    /** Whether Degrees Minutes Seconds (DMS) format is enabled, e.g. `51°30'26.64"N, 0°7'40.08"W`. */
    var enableDMS: Boolean = true,

    /** Language used for the what3words address returned from a successful lookup. */
    var language: W3WLanguage = W3WRFC5646Language.EN_GB
) : SearchConfig()