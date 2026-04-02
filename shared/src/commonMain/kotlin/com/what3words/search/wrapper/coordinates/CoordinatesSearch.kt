package com.what3words.search.wrapper.coordinates

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SimpleSearchPlugin

/**
 * Entry point for the coordinates search plugin.
 *
 * Implements [SimpleSearchPlugin] to provide coordinate-based what3words address lookup,
 * supporting Decimal Degrees (DD), Degrees Decimal Minutes (DDM), and
 * Degrees Minutes Seconds (DMS) formats.
 */
object CoordinatesSearch :
    SimpleSearchPlugin<CoordinatesSearchConfig, SearchProvider>() {

    /** Returns the default [CoordinatesSearchConfig] with all coordinate formats enabled. */
    override fun defaultConfig(): CoordinatesSearchConfig = CoordinatesSearchConfig()

    /**
     * Builds a [SearchProvider] using the given [config] and [textDataSource].
     *
     * @param config Configuration controlling which coordinate formats are enabled and the target language.
     * @param textDataSource Data source used to convert coordinates to what3words addresses.
     */
    override fun build(
        config: CoordinatesSearchConfig,
        textDataSource: W3WTextDataSource
    ): SearchProvider {
        return CoordinatesSearchProvider(textDataSource, config)
    }
}