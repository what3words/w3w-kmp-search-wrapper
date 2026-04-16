package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SimpleSearchPlugin

object MayBeAThreeWordAddressSearch :
    SimpleSearchPlugin<ThreeWordAddressSearchConfig, SearchProvider>() {

    override fun defaultConfig(): ThreeWordAddressSearchConfig = ThreeWordAddressSearchConfig()

    override fun build(
        config: ThreeWordAddressSearchConfig,
        textDataSource: W3WTextDataSource,
    ): SearchProvider = MayBeAThreeWordAddressSearchProvider(textDataSource, config)
}
