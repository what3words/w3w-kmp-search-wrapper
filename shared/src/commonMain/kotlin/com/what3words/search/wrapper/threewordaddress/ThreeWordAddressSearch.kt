package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SimpleSearchPlugin
import com.what3words.search.wrapper.core.W3WSearchClient

object ThreeWordAddressSearch :
    SimpleSearchPlugin<ThreeWordAddressSearchConfig, SearchProvider>() {

    override fun defaultConfig(): ThreeWordAddressSearchConfig = ThreeWordAddressSearchConfig()

    override fun build(
        config: ThreeWordAddressSearchConfig,
        textDataSource: W3WTextDataSource,
    ): SearchProvider = ThreeWordAddressSearchProvider(textDataSource, config)
}

/**
 * The live [ThreeWordAddressSearchConfig] for the installed three-word-address provider,
 * or `null` if [ThreeWordAddressSearch] was not installed.
 *
 * - Mutating fields on the returned instance (e.g. `clippedCountries`) takes effect on the
 *   next [W3WSearchClient.search] call.
 * - Assigning a non-null value replaces the config entirely. The assignment is a no-op if
 *   the plugin is not installed; check the getter first if you need to know.
 * - Assigning `null` throws [IllegalArgumentException]; the property is nullable only to
 *   express "plugin not installed" on read.
 */
var W3WSearchClient.threeWordAddressConfig: ThreeWordAddressSearchConfig?
    get() = config.providers.filterIsInstance<ThreeWordAddressSearchProvider>()
        .firstOrNull()?.config
    set(value) {
        requireNotNull(value) { "threeWordAddressConfig cannot be set to null" }
        val provider = config.providers.filterIsInstance<ThreeWordAddressSearchProvider>()
            .firstOrNull() ?: return
        provider.config = value
    }
