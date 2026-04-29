package com.what3words.search.wrapper.mapbox

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MapboxRetrieveResponse(
    @SerialName("features")
    val features: List<Feature> = emptyList(),
)

@Serializable
data class Feature(
    val geometry: Geometry,
)

@Serializable
data class Geometry(
    val coordinates: List<Double>,
)

