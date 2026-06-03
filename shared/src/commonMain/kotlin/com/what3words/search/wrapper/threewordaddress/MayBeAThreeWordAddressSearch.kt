package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SimpleSearchPlugin
import com.what3words.search.wrapper.core.W3WSearchClient

object MayBeAThreeWordAddressSearch :
    SimpleSearchPlugin<MayBeAThreeWordAddressSearchConfig, SearchProvider>() {

    override fun defaultConfig(): MayBeAThreeWordAddressSearchConfig =
        MayBeAThreeWordAddressSearchConfig()

    override fun build(
        config: MayBeAThreeWordAddressSearchConfig,
        textDataSource: W3WTextDataSource,
    ): SearchProvider = MayBeAThreeWordAddressSearchProvider(textDataSource, config)
}

/**
 * The live [MayBeAThreeWordAddressSearchConfig] for the installed provider, or `null` if
 * [MayBeAThreeWordAddressSearch] was not installed. Field mutations take effect on the next search.
 */
var W3WSearchClient.mayBeAThreeWordAddressConfig: MayBeAThreeWordAddressSearchConfig?
    get() = config.providers.filterIsInstance<MayBeAThreeWordAddressSearchProvider>()
        .firstOrNull()?.config
    set(value) {
        val provider = config.providers.filterIsInstance<MayBeAThreeWordAddressSearchProvider>()
            .firstOrNull() ?: return
        if (value != null) provider.config = value
    }
