package com.what3words.search.wrapper.core

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WResult
import com.what3words.search.wrapper.error.ProviderNotFoundException
import com.what3words.search.wrapper.error.ProviderNotResolvableException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * A unified client for executing searches across multiple [SearchProvider]s.
 *
 * Providers are dispatched by priority (higher number = higher priority). When a search is
 * executed, all capable providers within the highest-priority tier run concurrently and their
 * results are merged. If that tier yields no results, the next tier is tried, and so on.
 * Providers with the same priority are treated as a single tier.
 */
class W3WSearchClient {

    internal val config: Config

    /**
     * Idiomatic Kotlin constructor using a configuration DSL.
     *
     * @param textDataSource The data source used for text-based searches.
     * @param block DSL block to configure and install [SearchPlugin]s.
     */
    constructor(textDataSource: W3WTextDataSource, block: Config.() -> Unit) {
        config = Config(textDataSource).apply(block)
    }

    /**
     * Standard constructor initializing the client with the provided [textDataSource].
     *
     * @param textDataSource The data source used for text-based searches.
     */
    constructor(textDataSource: W3WTextDataSource) {
        this.config = Config(textDataSource)
    }

    /**
     * Executes a search query using installed providers, dispatched by priority tier.
     *
     * All capable providers within the highest-priority tier execute concurrently. Their results
     * are merged and returned if non-empty. Returns [W3WResult.Failure] with [ProviderNotFoundException]
     * if no provider can handle the query.
     *
     * @param query The search query string.
     * @return A [W3WResult] containing a merged list of [SearchResult]s.
     */
    @Throws(Exception::class)
    suspend fun search(query: String): W3WResult<List<SearchResult>> {
        for (tier in config.providerTiers()) {
            val capable = tier.filter { it.canHandle(query) }
            if (capable.isEmpty()) continue //If no provider can handle, try next tier

            val rawResults = coroutineScope {
                capable.map { async { it.executeSearch(query) } }.awaitAll()
            }

            val successes = rawResults.filterIsInstance<W3WResult.Success<List<SearchResult>>>()
            if (successes.isEmpty()) {
                // All capable providers failed — surface the first failure rather than silently
                // returning an empty success.
                return rawResults.first()
            }

            return W3WResult.Success(successes.flatMap { it.value })
        }

        return W3WResult.Failure(ProviderNotFoundException())
    }

    /**
     * Resolves a [SearchResult.SearchSuggestion] into a [SearchResult.ResolvedAddress].
     * The provider that originally supplied the suggestion must implement [ResolvableSearchProvider].
     *
     * @param data The suggestion to resolve.
     * @return A [W3WResult] containing the resolved address, or [W3WResult.Failure] with
     * [ProviderNotResolvableException] if the originating provider cannot be found or does not support resolution.
     */
    @Throws(Exception::class)
    suspend fun resolve(data: SearchResult.SearchSuggestion): W3WResult<SearchResult.ResolvedAddress> {
        val provider = config.providers.find { it.providerId == data.providerId }
        if (provider is ResolvableSearchProvider) {
            return provider.resolve(data)
        }

        return W3WResult.Failure(ProviderNotResolvableException())
    }

    /**
     * Configuration builder for [W3WSearchClient].
     *
     * @property textDataSource The data source used for text-based searches.
     */
    class Config(val textDataSource: W3WTextDataSource) {

        private data class PrioritizedProvider(val priority: Int, val provider: SearchProvider)

        private val _providers = mutableListOf<PrioritizedProvider>()

        /**
         * The flat list of installed [SearchProvider]s, sorted by descending priority.
         * Providers with the same priority preserve their installation order.
         */
        val providers: List<SearchProvider>
            get() = _providers.sortedByDescending { it.priority }.map { it.provider }

        /**
         * Returns providers grouped into priority tiers, highest priority first.
         * Each inner list contains all providers sharing the same priority value.
         */
        internal fun providerTiers(): List<List<SearchProvider>> =
            _providers
                .sortedByDescending { it.priority }
                .groupBy { it.priority }
                .values
                .map { tier -> tier.map { it.provider } }

        /**
         * Installs a [SearchPlugin] with the specified configuration and priority.
         * Hidden from Objective-C/Swift to encourage using strongly-typed provider builders.
         *
         * Providers with a higher [priority] value are tried before those with a lower value.
         * Providers sharing the same [priority] execute concurrently when a search is performed.
         *
         * @param plugin The plugin to install.
         * @param config The required configuration instance.
         * @param priority Dispatch priority; higher values run first.
         * @param configure An optional DSL block to apply further configurations.
         */
        @OptIn(ExperimentalObjCRefinement::class)
        @HiddenFromObjC
        fun <TConfig : Any, TProvider : SearchProvider> install(
            plugin: SearchPlugin<TConfig, TProvider>,
            config: TConfig,
            priority: Int,
            configure: TConfig.() -> Unit = {}
        ) {
            _providers.add(
                PrioritizedProvider(priority, plugin.build(config.apply(configure), textDataSource))
            )
        }

        /**
         * Installs a [SimpleSearchPlugin] that has a default configuration.
         * Hidden from Objective-C/Swift to encourage using strongly-typed provider builders.
         *
         * Providers with a higher [priority] value are tried before those with a lower value.
         * Providers sharing the same [priority] execute concurrently when a search is performed.
         *
         * @param plugin The plugin to install.
         * @param priority Dispatch priority; higher values run first.
         * @param configure An optional DSL block to apply further configurations.
         */
        @OptIn(ExperimentalObjCRefinement::class)
        @HiddenFromObjC
        fun <TConfig : Any, TProvider : SearchProvider> install(
            plugin: SimpleSearchPlugin<TConfig, TProvider>,
            priority: Int,
            configure: TConfig.() -> Unit = {}
        ) {
            _providers.add(
                PrioritizedProvider(
                    priority,
                    plugin.build(plugin.defaultConfig().apply(configure), textDataSource)
                )
            )
        }
    }
}
