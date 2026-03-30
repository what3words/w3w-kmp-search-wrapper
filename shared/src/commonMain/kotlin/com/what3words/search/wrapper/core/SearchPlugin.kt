package com.what3words.search.wrapper.core

import com.what3words.core.datasource.text.W3WTextDataSource

/**
 * Represents a plugin that builds a [SearchProvider] using a specific configuration type [TConfig].
 *
 * @param TConfig The type of configuration required to build the provider.
 * @param TProvider The type of [SearchProvider] returned by this plugin.
 */
abstract class SearchPlugin<TConfig : Any, TProvider : SearchProvider> {
    /**
     * Builds and returns a new [SearchProvider] instance.
     *
     * @param config The configuration instance to use.
     * @param textDataSource The data source used for text-based searches.
     * @return A configured [SearchProvider].
     */
    abstract fun build(config: TConfig, textDataSource: W3WTextDataSource): TProvider
}

/**
 * A [SearchPlugin] that provides a default configuration.
 * Allows omitting the configuration object when installing the plugin.
 */
abstract class SimpleSearchPlugin<TConfig : Any, TProvider : SearchProvider> : SearchPlugin<TConfig, TProvider>() {
    /**
     * Provides a default instance of [TConfig].
     */
    abstract fun defaultConfig(): TConfig
}