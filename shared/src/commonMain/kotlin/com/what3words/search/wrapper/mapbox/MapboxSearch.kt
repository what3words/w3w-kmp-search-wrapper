package com.what3words.search.wrapper.mapbox

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.search.wrapper.core.ResolvableSearchProvider
import com.what3words.search.wrapper.core.SearchPlugin
import com.what3words.search.wrapper.core.W3WSearchClient

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

/**
 * The live [MapboxConfig] for the installed provider, or `null` if [MapboxSearch] was not
 * installed.
 *
 * [MapboxConfig] is an immutable `data class`; swap the entire config by assigning a fresh
 * `copy(...)` to change values at runtime, e.g.:
 *
 * ```kotlin
 * client.mapboxConfig = client.mapboxConfig?.copy(maxResults = 10)
 * ```
 *
 * Assignment is a no-op if the plugin is not installed. Assigning `null` throws
 * [IllegalArgumentException] — the property is nullable only on read.
 */
var W3WSearchClient.mapboxConfig: MapboxConfig?
    get() = config.providers.filterIsInstance<MapboxSearchProvider>().firstOrNull()?.config
    set(value) {
        requireNotNull(value) { "mapboxConfig cannot be set to null" }
        val provider = config.providers.filterIsInstance<MapboxSearchProvider>().firstOrNull()
            ?: return
        provider.config = value
    }
