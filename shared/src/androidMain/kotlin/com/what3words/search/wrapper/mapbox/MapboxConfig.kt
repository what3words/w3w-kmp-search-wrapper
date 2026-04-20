package com.what3words.search.wrapper.mapbox

import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.core.types.language.W3WRFC5646Language

/**
 * Configuration for [MapboxSearchProvider].
 *
 * @property apiKey Mapbox access token for authenticating requests.
 * @property language RFC 5646 language tag passed to the Mapbox Geocoding API and used for
 *   what3words coordinate conversion. Defaults to [W3WRFC5646Language.EN_GB].
 * @property minQueryLength Minimum characters before a search is dispatched. Defaults to `3`.
 * @property maxResults Maximum number of geocoding features returned per search. Defaults to `5`.
 * @property autoComplete When autocomplete is enabled, results will be included that start with the
 *   requested string, rather than responses that match it exactly. Defaults to `true`.
 * @property includedRegionCodes CLDR two-character region codes to restrict results (e.g. `["US", "GB"]`). Defaults to an empty list (no restriction).
 * @property fuzzyMatch Specify whether the Geocoding API should try approximate, as well as exact,
 *   matching when performing searches. Defaults to `true`.
 * @property focus Bias the response to favor results that are closer to this location.
 */
data class MapboxConfig(
    val apiKey: String,
    val language: W3WRFC5646Language = W3WRFC5646Language.EN_GB,
    val minQueryLength: Int = 3,
    val maxResults: Int = 5,
    val autoComplete: Boolean = true,
    val includedRegionCodes: List<String> = emptyList(),
    val fuzzyMatch: Boolean = true,
    val focus: W3WCoordinates? = null
)
