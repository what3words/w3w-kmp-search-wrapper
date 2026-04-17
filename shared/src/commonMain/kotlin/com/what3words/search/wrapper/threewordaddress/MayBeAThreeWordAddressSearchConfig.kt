package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.types.options.W3WAutosuggestOptions

/** Configuration for [MayBeAThreeWordAddressSearchProvider]. */
class MayBeAThreeWordAddressSearchConfig : AutosuggestSearchConfig()

internal fun MayBeAThreeWordAddressSearchConfig.toAutosuggestOptions(): W3WAutosuggestOptions =
    buildBaseOptions().build()
