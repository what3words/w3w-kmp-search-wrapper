package com.what3words.search.wrapper.mapbox

import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.core.types.language.W3WLanguage
import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.search.wrapper.core.SearchConfig

/**
 * Configuration for [MapboxSearchProvider].
 *
 * @property apiKey Mapbox access token for authenticating requests.
 * @property language Language passed to the Mapbox Search Box API and used for what3words
 *   coordinate conversion. Defaults to [W3WRFC5646Language.EN_GB].
 * @property minQueryLength Minimum characters before a search is dispatched. Defaults to `3`.
 * @property maxResults Maximum number of search suggestions returned per request. Defaults to `5`.
 * @property includedRegionCodes CLDR two-character region codes to restrict results (e.g. `["US", "GB"]`). Defaults to an empty list (no restriction).
 * @property focus Bias the response to favor results that are closer to this location.
 */
data class MapboxConfig(
    val apiKey: String,
    val language: W3WRFC5646Language = W3WRFC5646Language.EN_GB,
    val minQueryLength: Int = 3,
    val maxResults: Int = 5,
    val includedRegionCodes: List<String> = emptyList(),
    val focus: W3WCoordinates? = null,
) : SearchConfig()
