package com.what3words.search.wrapper.googleplaces

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.search.wrapper.core.ResolvableSearchProvider
import com.what3words.search.wrapper.core.SearchPlugin


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
