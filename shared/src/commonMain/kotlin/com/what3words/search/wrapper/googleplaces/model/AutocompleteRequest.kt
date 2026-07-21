package com.what3words.search.wrapper.googleplaces.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AutocompleteRequest(
    val input: String,
    /** Billing session token (UUID v4). Null when session tokens are disabled. */
    val sessionToken: String? = null,
    val locationBias: LocationBiasRequest? = null,
    val origin: LatLng? = null,
    val includedRegionCodes: List<String>? = null,
    val languageCode: String? = null,
)

@Serializable
internal data class LocationBiasRequest(
    val circle: CircleRequest? = null,
    val rectangle: RectangleRequest? = null,
)

@Serializable
internal data class CircleRequest(
    val center: LatLng,
    /** Radius in metres, as required by the Places API. */
    val radius: Double,
)

@Serializable
internal data class RectangleRequest(
    /** Maps to the southwest corner of the rectangle. */
    val low: LatLng,
    /** Maps to the northeast corner of the rectangle. */
    val high: LatLng,
)
