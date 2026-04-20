package com.what3words.search.wrapper.googleplaces

import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.core.types.language.W3WRFC5646Language

/**
 * Configuration for [GooglePlacesSearchProvider].
 *
 * @property apiKey Google Places API key for authenticating requests.
 * @property language RFC 5646 language for autocomplete and what3words conversion. Defaults to [W3WRFC5646Language.EN_GB].
 * @property useSessionTokens When `true`, a UUID session token groups each autocomplete call
 *   with its paired place-details fetch into one billing session, rotating after every resolve. Defaults to `true`.
 * @property minQueryLength Minimum characters before a search is dispatched. Defaults to `3`.
 * @property maxResults Maximum autocomplete suggestions returned per search. Defaults to `5`.
 * @property headers Additional HTTP headers sent with every request.
 * @property locationBias Biases autocomplete results toward a circular or rectangular region. Null means no bias.
 * @property origin Origin point used to calculate straight-line distance to each suggestion. Null means no origin.
 *   In certain cases, distanceMeters is missing from the response body, even when origin is included in the request.
 *   See more: [Distance missing from response](https://developers.google.com/maps/documentation/places/web-service/place-autocomplete#distance-missing-from-response)
 * @property includedRegionCodes CLDR two-character region codes to restrict results (e.g. `["US", "GB"]`). Empty means no restriction.
 */
expect class GooglePlacesConfig(
    apiKey: String,
    language: W3WRFC5646Language = W3WRFC5646Language.EN_GB,
    useSessionTokens: Boolean = true,
    minQueryLength: Int = 3,
    maxResults: Int = 5,
    locationBias: LocationBias? = null,
    origin: W3WCoordinates? = null,
    includedRegionCodes: List<String> = emptyList(),
    headers: Map<String, String?> = emptyMap(),
) {
    val apiKey: String
    val language: W3WRFC5646Language
    val useSessionTokens: Boolean
    val minQueryLength: Int
    val maxResults: Int
    val headers: Map<String, String?>
    val locationBias: LocationBias?
    val origin: W3WCoordinates?
    val includedRegionCodes: List<String>

    fun copy(
        apiKey: String = this.apiKey,
        language: W3WRFC5646Language = this.language,
        useSessionTokens: Boolean = this.useSessionTokens,
        minQueryLength: Int = this.minQueryLength,
        maxResults: Int = this.maxResults,
        headers: Map<String, String?> = this.headers,
        locationBias: LocationBias? = this.locationBias,
        origin: W3WCoordinates? = this.origin,
        includedRegionCodes: List<String> = this.includedRegionCodes,
    ): GooglePlacesConfig
}
