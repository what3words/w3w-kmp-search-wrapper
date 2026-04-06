package com.what3words.search.wrapper.mapbox

import com.what3words.core.types.common.W3WError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
internal data class MapboxFeatureCollection(
    val features: List<MapboxFeature> = emptyList()
)

/**
 * A single geocoding result feature.
 *
 * @property id Unique Mapbox feature identifier (e.g. `"place.17652"`).
 * @property text Primary display name for the place (e.g. `"Hanoi"`).
 * @property placeName Full human-readable name including parent contexts (e.g. `"Hanoi, Vietnam"`).
 * @property center `[longitude, latitude]` coordinate pair for the place.
 * @property context Ordered list of parent administrative contexts (region, country, etc.).
 */
@Serializable
internal data class MapboxFeature(
    val id: String = "",
    val text: String = "",
    @SerialName("place_name")
    val placeName: String = "",
    val center: List<Double> = emptyList(),
    val context: List<MapboxContext> = emptyList(),
)

@Serializable
internal data class MapboxContext(
    val id: String = "",
    val text: String = "",
)
