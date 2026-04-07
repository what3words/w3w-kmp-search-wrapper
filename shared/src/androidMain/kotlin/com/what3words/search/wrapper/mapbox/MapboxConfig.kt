package com.what3words.search.wrapper.mapbox

import com.what3words.core.types.language.W3WRFC5646Language

/**
 * Configuration for [MapboxSearchProvider].
 *
 * @property apiKey Mapbox access token for authenticating requests.
 * @property language RFC 5646 language tag passed to the Mapbox Geocoding API and used for
 *   what3words coordinate conversion. Defaults to [W3WRFC5646Language.EN_GB].
 * @property minQueryLength Minimum characters before a search is dispatched. Defaults to `3`.
 * @property maxResults Maximum number of geocoding features returned per search. Defaults to `5`.
 */
data class MapboxConfig(
    val apiKey: String,
    val language: W3WRFC5646Language = W3WRFC5646Language.EN_GB,
    val minQueryLength: Int = 3,
    val maxResults: Int = 5,
)
