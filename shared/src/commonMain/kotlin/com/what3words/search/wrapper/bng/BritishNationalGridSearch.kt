package com.what3words.search.wrapper.bng

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.search.wrapper.core.SimpleSearchPlugin

/**
 * Entry point for the British National Grid search plugin.
 *
 * Constructs a [BritishNationalGridSearchProvider] from a [BritishNationalGridSearchConfig]
 * and a [W3WTextDataSource], enabling what3words address resolution from OS grid references
 * and easting/northing coordinates.
 */
object BritishNationalGridSearch :
    SimpleSearchPlugin<BritishNationalGridSearchConfig, BritishNationalGridSearchProvider>() {
    override fun defaultConfig(): BritishNationalGridSearchConfig =
        BritishNationalGridSearchConfig()

    override fun build(
        config: BritishNationalGridSearchConfig,
        textDataSource: W3WTextDataSource
    ): BritishNationalGridSearchProvider {
        return BritishNationalGridSearchProvider(textDataSource, config)
    }
}