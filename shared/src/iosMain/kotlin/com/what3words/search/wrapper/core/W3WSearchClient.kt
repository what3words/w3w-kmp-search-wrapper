package com.what3words.search.wrapper.core

/**
 * Installs a new [SearchPlugin] with its specific [config] and optional [priority].
 *
 * Providers with a higher [priority] value are tried before those with a lower value.
 * Providers sharing the same [priority] execute concurrently when a search is performed.
 *
 * @param plugin The search plugin to add.
 * @param config The configuration for the given search plugin.
 * @param priority Dispatch priority; higher values run first. Defaults to 0.
 */
fun <TConfig : Any, TProvider : SearchProvider> W3WSearchClient.addPlugin(
    plugin: SearchPlugin<TConfig, TProvider>,
    config: TConfig,
    priority: Int
) {
    this.config.install(plugin = plugin, config = config, priority = priority)
}

/**
 * Installs a [SimpleSearchPlugin] using its default configuration.
 *
 * Providers with a higher [priority] value are tried before those with a lower value.
 * Providers sharing the same [priority] execute concurrently when a search is performed.
 *
 * @param plugin The search plugin to add.
 * @param priority Dispatch priority; higher values run first. Defaults to 0.
 */
fun <TConfig : Any, TProvider : SearchProvider> W3WSearchClient.addPlugin(
    plugin: SimpleSearchPlugin<TConfig, TProvider>,
    priority: Int
) {
    this.config.install(plugin = plugin, priority = priority)
}
