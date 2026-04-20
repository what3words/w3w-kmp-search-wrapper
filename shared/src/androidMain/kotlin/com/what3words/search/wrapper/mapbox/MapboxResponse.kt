package com.what3words.search.wrapper.mapbox

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MapboxFeatureCollection(
    val features: List<MapboxFeature> = emptyList()
)

/**
 * A single geocoding result feature.
 *
 * @property id Unique Mapbox feature identifier (e.g. `"address.4839674036689214"`).
 * @property text Primary display name for the place (e.g. `"Lê Thánh Tôn"`).
 * @property placeName Full human-readable name including parent contexts (e.g. `"Lê Thánh Tôn, 71000, Bến Thành, Ho Chi Minh City, Vietnam"`).
 * @property center `[longitude, latitude]` coordinate pair for the place.
 * @property context Ordered list of parent administrative contexts (region, country, etc.).
 */
@Serializable
internal data class MapboxFeature(
    val id: String = "",
    val text: String = "",
    @SerialName("place_name")
    val placeName: String = "",
    val address: String? = null,
    val center: List<Double> = emptyList(),
    val context: List<MapboxContext> = emptyList(),
)

@Serializable
internal data class MapboxContext(
    val id: String = "",
    val text: String = "",
    @SerialName("short_code")
    val shortCode: String? = null,
)
