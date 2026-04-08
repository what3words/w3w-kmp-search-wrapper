package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.types.domain.W3WCountry
import com.what3words.core.types.language.W3WLanguage
import com.what3words.core.types.language.W3WRFC5646Language

/** Configuration for [ThreeWordAddressSearchProvider]. */
data class ThreeWordAddressSearchConfig(
    /** Number of autosuggest results to return. */
    var maxResults: Int = 3,

    /** Countries to clip results to. Empty means no clipping. */
    var clipToCountry: List<W3WCountry> = emptyList(),

    /** Language for returned what3words addresses. */
    var language: W3WLanguage = W3WRFC5646Language.EN_GB,

    /** Prefer suggestions on land instead of at sea. */
    var preferLand: Boolean = false,
)