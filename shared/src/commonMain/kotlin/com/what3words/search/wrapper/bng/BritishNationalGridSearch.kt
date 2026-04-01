package com.what3words.search.wrapper.bng

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SimpleSearchPlugin

/**
 * Entry point for the British National Grid search plugin.
 *
 * Builds a [SearchProvider] from a [BritishNationalGridSearchConfig] and a [W3WTextDataSource],
 * enabling what3words address resolution from OS grid references and easting/northing coordinates.
 */
object BritishNationalGridSearch :
    SimpleSearchPlugin<BritishNationalGridSearchConfig, SearchProvider>() {

    override fun defaultConfig(): BritishNationalGridSearchConfig =
        BritishNationalGridSearchConfig()

    override fun build(
        config: BritishNationalGridSearchConfig,
        textDataSource: W3WTextDataSource
    ): SearchProvider {
        return BritishNationalGridSearchProvider(textDataSource, config)
    }
}