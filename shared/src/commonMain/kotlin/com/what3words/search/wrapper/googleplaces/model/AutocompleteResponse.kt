package com.what3words.search.wrapper.googleplaces.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
internal data class AutocompleteResponse(
    val suggestions: List<Suggestion> = emptyList()
)

@Serializable
internal data class Suggestion(
    val placePrediction: PlacePrediction? = null
)

@Serializable
internal data class PlacePrediction(
    val placeId: String = "",
    val text: LocalizedText? = null,
    val structuredFormat: StructuredFormat? = null,
    @SerialName("distanceMeters")
    val distanceToOrigin: Double? = null,
    val types: List<String> = emptyList(),
)

@Serializable
internal data class StructuredFormat(
    val mainText: LocalizedText? = null,
    val secondaryText: LocalizedText? = null
)

@Serializable
internal data class LocalizedText(
    val text: String = "",
)

