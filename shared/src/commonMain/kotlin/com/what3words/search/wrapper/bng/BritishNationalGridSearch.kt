package com.what3words.search.wrapper.bng

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SimpleSearchPlugin
import com.what3words.search.wrapper.core.W3WSearchClient

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

/**
 * The live [BritishNationalGridSearchConfig] for the installed provider, or `null` if
 * [BritishNationalGridSearch] was not installed.
 *
 * Field mutations take effect on the next search. Assigning a non-null value replaces the
 * config; assignment is a no-op if the plugin is not installed. Assigning `null` throws
 * [IllegalArgumentException] — the property is nullable only on read.
 */
var W3WSearchClient.britishNationalGridConfig: BritishNationalGridSearchConfig?
    get() = config.providers.filterIsInstance<BritishNationalGridSearchProvider>()
        .firstOrNull()?.config
    set(value) {
        requireNotNull(value) { "britishNationalGridConfig cannot be set to null" }
        val provider = config.providers.filterIsInstance<BritishNationalGridSearchProvider>()
            .firstOrNull() ?: return
        provider.config = value
    }