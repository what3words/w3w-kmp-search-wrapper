package com.what3words.search.wrapper.core

/**
 * Installs a plugin captured in a [SearchPluginHandle] into this client.
 *
 * This is the Swift/Objective-C-friendly entry point. Create a handle via
 * [SimpleSearchPlugin.asHandle] (uses default config) or [SearchPlugin.asHandle] (explicit config),
 * then pass it here.
 *
 * Example (Swift):
 * ```swift
 * searchClient.addPlugin(plugin: BritishNationalGridSearch.shared.asHandle(), priority: 9)
 * searchClient.addPlugin(plugin: SomePlugin.shared.asHandle(config: MyConfig()), priority: 5)
 * ```
 *
 * Providers with a higher [priority] value are tried before those with a lower value.
 * Providers sharing the same [priority] execute concurrently when a search is performed.
 *
 * @param plugin A [SearchPluginHandle] wrapping the plugin and its configuration.
 * @param priority Dispatch priority; higher values run first.
 */
fun W3WSearchClient.addPlugin(plugin: SearchPluginHandle, priority: Int) {
    plugin.doInstall(this.config, priority)
}
