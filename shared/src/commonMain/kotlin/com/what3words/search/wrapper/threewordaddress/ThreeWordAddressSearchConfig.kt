package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.types.options.W3WAutosuggestOptions

/** Configuration for [ThreeWordAddressSearchProvider]. */
class ThreeWordAddressSearchConfig : AutosuggestSearchConfig() {
    /** Number of autosuggest results to return. */
    var maxResults: Int = 3

    /** If true, space-separated queries (e.g. "index home raft") are recognised as three-word
     * addresses in addition to the standard dot-separated form.
     * Defaults to false. */
    var allowSpaceSeparator: Boolean = false
}

internal fun ThreeWordAddressSearchConfig.toAutosuggestOptions(): W3WAutosuggestOptions =
    buildBaseOptions().nResults(maxResults).build()
