package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.types.domain.W3WCountry
import com.what3words.core.types.geometry.W3WCircle
import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.core.types.geometry.W3WPolygon
import com.what3words.core.types.geometry.W3WRectangle
import com.what3words.core.types.language.W3WLanguage
import com.what3words.core.types.options.W3WAutosuggestInputType
import com.what3words.core.types.options.W3WAutosuggestOptions
import com.what3words.search.wrapper.core.SearchResult

/** Configuration for [ThreeWordAddressSearchProvider]. */
data class ThreeWordAddressSearchConfig(
    /** Number of autosuggest results to return. */
    var maxResults: Int = 3,

    /** Countries to clip results to. Empty means no clipping. */
    var clippedCountries: List<W3WCountry> = emptyList(),

    /** Fallback language used when the autosuggest input is ambiguous or unclear. */
    var fallbackLanguage: W3WLanguage? = null,

    /** Prefer suggestions on land instead of at sea. */
    var preferLand: Boolean = true,

    /** Focus coordinates used to bias autosuggest ranking. */
    var focus: W3WCoordinates? = null,

    /** A circle to clip the autosuggestions to. */
    var clipToCircle: W3WCircle? = null,

    /**A bounding box to clip the autosuggestions to. */
    var clipToBoundingBox: W3WRectangle? = null,

    /** A polygon to clip the autosuggestions to. */
    var clipToPolygon: W3WPolygon? = null,

    /** If true, each suggestion includes coordinates and is returned as [SearchResult.ResolvedAddress];
     * otherwise [SearchResult.SearchSuggestion].
     * Defaults to false. */
    var includeCoordinates: Boolean = false,
)

internal fun ThreeWordAddressSearchConfig.toAutosuggestOptions(): W3WAutosuggestOptions =
    W3WAutosuggestOptions.Builder()
        .language(fallbackLanguage)
        .nResults(maxResults)
        .apply {
            clippedCountries.takeIf { it.isNotEmpty() }?.let { clipToCountry(*it.toTypedArray()) }
            clipToCircle?.let { clipToCircle(it) }
            clipToBoundingBox?.let { clipToBoundingBox(it) }
            clipToPolygon?.let { clipToPolygon(it) }
        }
        .focus(focus)
        .preferLand(preferLand)
        .inputType(W3WAutosuggestInputType.TEXT)
        .includeCoordinates(includeCoordinates)
        .build()
