package com.what3words.search.wrapper.fixtures

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.domain.W3WAddress
import com.what3words.core.types.domain.W3WCountry
import com.what3words.core.types.language.W3WProprietaryLanguage
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.SimpleSearchPlugin
import com.what3words.search.wrapper.core.W3WSearchClient
import com.what3words.search.wrapper.fake.fakeDataSource

/** Wraps any [SearchProvider] in a [SimpleSearchPlugin] that ignores config and data source. */
internal fun pluginFor(provider: SearchProvider) =
    object : SimpleSearchPlugin<Unit, SearchProvider>() {
        override fun defaultConfig() = Unit
        override fun build(config: Unit, textDataSource: W3WTextDataSource) = provider
    }

/** Creates a [W3WAddress] with fixed test values. */
internal fun fakeAddress() = W3WAddress(
    words = "filled.count.soap",
    center = null,
    square = null,
    language = W3WProprietaryLanguage("en", null, null, null),
    country = W3WCountry("GB"),
    nearestPlace = "London"
)

/** Creates a [SearchResult.SearchSuggestion] for the given [providerId] and optional [query]. */
internal fun suggestion(providerId: String, query: String = "test") =
    SearchResult.SearchSuggestion(query = query, providerId = providerId, extras = emptyMap())

/** Builds a [W3WSearchClient] using [fakeDataSource] and the provided DSL [block]. */
internal fun buildClient(block: W3WSearchClient.Config.() -> Unit) =
    W3WSearchClient(fakeDataSource, block)
