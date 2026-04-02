package com.what3words.search.wrapper.fixtures

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.domain.W3WAddress
import com.what3words.core.types.domain.W3WCountry
import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.core.types.geometry.W3WRectangle
import com.what3words.core.types.language.W3WProprietaryLanguage
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.SimpleSearchPlugin
import com.what3words.search.wrapper.core.W3WSearchClient
import com.what3words.search.wrapper.fake.FakeW3WTextDataSource

/** Wraps any [SearchProvider] in a [SimpleSearchPlugin] that ignores config and data source. */
internal fun pluginFor(provider: SearchProvider) =
    object : SimpleSearchPlugin<Unit, SearchProvider>() {
        override fun defaultConfig() = Unit
        override fun build(config: Unit, textDataSource: W3WTextDataSource) = provider
    }

/** Creates a [W3WAddress] with fixed test values. */
internal fun fakeAddress() = W3WAddress(
    words = "filled.count.soap",
    center = W3WCoordinates(lat = 51.520847, lng = -0.195521),
    square = W3WRectangle(
        southwest = W3WCoordinates(lat = 51.520833, lng = -0.195543),
        northeast = W3WCoordinates(lat = 51.52086, lng = -0.195499)
    ),
    language = W3WProprietaryLanguage("en", null, null, null),
    country = W3WCountry("GB"),
    nearestPlace = "Bayswater, London"
)

/** Creates a [SearchResult.SearchSuggestion] for the given [providerId] and optional [query]. */
internal fun suggestion(
    providerId: String,
    query: String = "test",
    title: String = "title",
    subtitle: String = "subtitle"
) = SearchResult.SearchSuggestion(
    query = query, providerId = providerId, extras = emptyMap(),
    title = title,
    subtitle = subtitle
)

/** Builds a [W3WSearchClient] using [FakeW3WTextDataSource] and the provided DSL [block]. */
internal fun buildClient(block: W3WSearchClient.Config.() -> Unit) =
    W3WSearchClient(FakeW3WTextDataSource(), block)
