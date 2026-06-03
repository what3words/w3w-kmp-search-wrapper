package com.what3words.search.wrapper.googleplaces

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.search.wrapper.core.ResolvableSearchProvider
import com.what3words.search.wrapper.core.SearchPlugin
import com.what3words.search.wrapper.core.W3WSearchClient


/**
 * [SearchPlugin] that builds and installs a [GooglePlacesSearchProvider] into a `W3WSearchClient`.
 *
 * ```kotlin
 * W3WSearchClient(textDataSource) {
 *     install(GooglePlacesSearch, GooglePlacesConfig(apiKey = "YOUR_API_KEY"), priority = 1)
 * }
 * ```
 */
object GooglePlacesSearch : SearchPlugin<GooglePlacesConfig, ResolvableSearchProvider>() {

    override fun build(
        config: GooglePlacesConfig,
        textDataSource: W3WTextDataSource,
    ): ResolvableSearchProvider = GooglePlacesSearchProvider(config, textDataSource)
}

/**
 * The live [GooglePlacesConfig] for the installed provider, or `null` if [GooglePlacesSearch]
 * was not installed.
 *
 * [GooglePlacesConfig] is immutable; swap the entire config by assigning a fresh `copy(...)` to
 * change values at runtime, e.g.:
 *
 * ```kotlin
 * client.googlePlacesConfig = client.googlePlacesConfig?.copy(includedRegionCodes = listOf("GB"))
 * ```
 */
var W3WSearchClient.googlePlacesConfig: GooglePlacesConfig?
    get() = config.providers.filterIsInstance<GooglePlacesSearchProvider>()
        .firstOrNull()?.config
    set(value) {
        val provider = config.providers.filterIsInstance<GooglePlacesSearchProvider>()
            .firstOrNull() ?: return
        if (value != null) provider.config = value
    }
