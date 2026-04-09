package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.types.domain.W3WCountry
import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.core.types.language.W3WLanguage
import com.what3words.core.types.options.W3WAutosuggestInputType
import com.what3words.core.types.options.W3WAutosuggestOptions

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
)

internal fun ThreeWordAddressSearchConfig.toAutosuggestOptions(): W3WAutosuggestOptions =
    W3WAutosuggestOptions.Builder()
        .language(fallbackLanguage)
        .nResults(maxResults)
        .apply {
            if (clippedCountries.isNotEmpty()) {
                clipToCountry(*clippedCountries.toTypedArray())
            }
        }
        .focus(focus)
        .preferLand(preferLand)
        .inputType(W3WAutosuggestInputType.TEXT)
        .build()