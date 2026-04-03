package com.what3words.search.wrapper.googleplaces.model

import kotlinx.serialization.Serializable

@Serializable
internal data class PlaceDetailsResponse(
    val id: String = "",
    val formattedAddress: String = "",
    val location: LatLng? = null,
    val displayName: LocalizedText? = null
)

@Serializable
internal data class LatLng(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)