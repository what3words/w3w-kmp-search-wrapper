package com.what3words.search.wrapper.core

/**
 * A type-erased, non-generic handle for a [SearchPlugin] that captures both the plugin and its
 * configuration as a closure. This is the Swift/Objective-C-friendly alternative to the generic
 * [W3WSearchClient.addPlugin] overloads, which cannot be called from Swift due to Kotlin/Native
 * substituting upper bounds for type parameters in the generated ObjC header (causing generic
 * invariance mismatches).
 *
 * Create a handle via [SimpleSearchPlugin.asHandle] or [SearchPlugin.asHandle], then pass it
 * to [W3WSearchClient.addPlugin].
 */
class SearchPluginHandle internal constructor(
    internal val doInstall: (W3WSearchClient.Config, Int) -> Unit
)

/**
 * Creates a [SearchPluginHandle] for this [SimpleSearchPlugin] using its default configuration.
 *
 * @return A non-generic [SearchPluginHandle] that can be passed to [W3WSearchClient.addPlugin].
 */
fun <TConfig : SearchConfig, TProvider : SearchProvider> SimpleSearchPlugin<TConfig, TProvider>.asHandle(): SearchPluginHandle {
    val plugin = this
    return SearchPluginHandle { config, priority ->
        config.install(plugin, priority)
    }
}

/**
 * Creates a [SearchPluginHandle] for this [SearchPlugin] with the provided [config].
 *
 * @param config The configuration to use when building the provider.
 * @return A non-generic [SearchPluginHandle] that can be passed to [W3WSearchClient.addPlugin].
 */
fun <TConfig : SearchConfig, TProvider : SearchProvider> SearchPlugin<TConfig, TProvider>.asHandle(
    config: TConfig
): SearchPluginHandle {
    val plugin = this
    return SearchPluginHandle { clientConfig, priority ->
        clientConfig.install(plugin, config, priority)
    }
}
