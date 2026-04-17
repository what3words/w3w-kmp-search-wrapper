package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SimpleSearchPlugin

object MayBeAThreeWordAddressSearch :
    SimpleSearchPlugin<MayBeAThreeWordAddressSearchConfig, SearchProvider>() {

    override fun defaultConfig(): MayBeAThreeWordAddressSearchConfig =
        MayBeAThreeWordAddressSearchConfig()

    override fun build(
        config: MayBeAThreeWordAddressSearchConfig,
        textDataSource: W3WTextDataSource,
    ): SearchProvider = MayBeAThreeWordAddressSearchProvider(textDataSource, config)
}
