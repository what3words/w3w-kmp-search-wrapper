package com.what3words.search.wrapper.googleplaces

import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.core.types.language.W3WRFC5646Language
import platform.Foundation.NSBundle

private const val HEADER_IOS_BUNDLE_ID = "X-Ios-Bundle-Identifier"

actual class GooglePlacesConfig actual constructor(
    actual val apiKey: String,
    actual val language: W3WRFC5646Language,
    actual val useSessionTokens: Boolean,
    actual val minQueryLength: Int,
    actual val maxResults: Int,
    actual val locationBias: LocationBias?,
    actual val origin: W3WCoordinates?,
    actual val includedRegionCodes: List<String>,
    headers: Map<String, String?>,
) {
    private val baseHeaders: Map<String, String?> = headers

    actual val headers: Map<String, String?>
        get() = buildMap {
            putAll(baseHeaders)
            put(HEADER_IOS_BUNDLE_ID, NSBundle.mainBundle.bundleIdentifier)
        }

    actual fun copy(
        apiKey: String,
        language: W3WRFC5646Language,
        useSessionTokens: Boolean,
        minQueryLength: Int,
        maxResults: Int,
        headers: Map<String, String?>,
        locationBias: LocationBias?,
        origin: W3WCoordinates?,
        includedRegionCodes: List<String>,
    ): GooglePlacesConfig = GooglePlacesConfig(
        apiKey = apiKey,
        language = language,
        useSessionTokens = useSessionTokens,
        minQueryLength = minQueryLength,
        maxResults = maxResults,
        headers = headers,
        locationBias = locationBias,
        origin = origin,
        includedRegionCodes = includedRegionCodes,
    )
}
