package com.what3words.search.wrapper.mapbox

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MapboxSearchResponse(
    val suggestions: List<Suggestion>
)

@Serializable
data class Suggestion(
    @SerialName("address")
    val address: String? = null,
    @SerialName("context")
    val context: Context,
    @SerialName("feature_type")
    val featureType: String,
    @SerialName("full_address")
    val fullAddress: String? = null,
    @SerialName("language")
    val language: String,
    @SerialName("mapbox_id")
    val mapboxId: String,
    @SerialName("name")
    val name: String,
    @SerialName("place_formatted")
    val placeFormatted: String,
)

@Serializable
data class Context(
    @SerialName("country")
    val country: Country? = null,
    @SerialName("place")
    val place: Place? = null,
)


@Serializable
data class Country(
    @SerialName("country_code")
    val countryCode: String? = null,
    @SerialName("country_code_alpha_3")
    val countryCodeAlpha3: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("name")
    val name: String? = null
)

@Serializable
data class Place(
    @SerialName("id")
    val id: String? = null,
    @SerialName("name")
    val name: String? = null
)
