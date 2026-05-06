package com.what3words.search.wrapper.mapbox

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.search.wrapper.core.ResolvableSearchProvider
import com.what3words.search.wrapper.core.SearchPlugin

/**
 * [SearchPlugin] that builds and installs a [MapboxSearchProvider] into a `W3WSearchClient`.
 *
 * ```kotlin
 * W3WSearchClient(textDataSource) {
 *     install(MapboxSearch, MapboxConfig(apiKey = "YOUR_MAPBOX_TOKEN"), priority = 1)
 * }
 * ```
 */
object MapboxSearch : SearchPlugin<MapboxConfig, ResolvableSearchProvider>() {

    override fun build(
        config: MapboxConfig,
        textDataSource: W3WTextDataSource,
    ): ResolvableSearchProvider = MapboxSearchProvider(config, textDataSource)
}
