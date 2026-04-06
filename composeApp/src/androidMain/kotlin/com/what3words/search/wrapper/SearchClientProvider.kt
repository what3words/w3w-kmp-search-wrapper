package com.what3words.search.wrapper

import android.content.Context
import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.search.wrapper.bng.BritishNationalGridSearch
import com.what3words.search.wrapper.coordinates.CoordinatesSearch
import com.what3words.search.wrapper.core.W3WSearchClient
import com.what3words.search.wrapper.googleplaces.GooglePlacesConfig
import com.what3words.search.wrapper.googleplaces.GooglePlacesSearch
import com.what3words.search.wrapper.mapbox.MapboxConfig
import com.what3words.search.wrapper.mapbox.MapboxSearch

/** The map provider used to back the external place search. */
enum class MapProvider { Google, Mapbox }

class SearchClientProvider(
    private val context: Context,
    private val textDataSource: W3WTextDataSource,
) {
    /** Builds a [W3WSearchClient] with [CoordinatesSearch] pre-installed at the highest priority. */
    private fun buildClient(block: W3WSearchClient.Config.() -> Unit): W3WSearchClient =
        W3WSearchClient(textDataSource) {
            install(BritishNationalGridSearch, priority = 10)
            install(CoordinatesSearch, priority = 9)
            block()
        }

    private val googleMapsClient: W3WSearchClient by lazy {
        buildClient {
            install(
                plugin = GooglePlacesSearch,
                config = GooglePlacesConfig(context = context, apiKey = BuildConfig.PLACES_API),
                priority = 1,
            )
        }
    }

    private val mapboxClient: W3WSearchClient by lazy {
        buildClient {
            install(
                plugin = MapboxSearch,
                config = MapboxConfig(apiKey = BuildConfig.MAPBOX_API),
                priority = 1,
            )
        }
    }

    /** Returns the [W3WSearchClient] for the given [MapProvider]. */
    fun clientFor(provider: MapProvider): W3WSearchClient = when (provider) {
        MapProvider.Google -> googleMapsClient
        MapProvider.Mapbox -> mapboxClient
    }
}
