package com.what3words.search.wrapper.googleplaces

import com.what3words.core.types.common.W3WError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.Serializable


// ── Autocomplete request ──────────────────────────────────────────────────────

@Serializable
internal data class AutocompleteRequest(
    val input: String,
    /** Billing session token (UUID v4). Null when session tokens are disabled. */
    val sessionToken: String? = null
)

// ── Autocomplete response ─────────────────────────────────────────────────────

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
    val place: String = "",
    val placeId: String = "",
    val text: LocalizedText? = null,
    val structuredFormat: StructuredFormat? = null,
    val types: List<String> = emptyList()
)

@Serializable
internal data class StructuredFormat(
    val mainText: LocalizedText? = null,
    val secondaryText: LocalizedText? = null
)

@Serializable
internal data class LocalizedText(
    val text: String = "",
    val matches: List<StringRange> = emptyList()
)

@Serializable
internal data class StringRange(
    val startOffset: Int = 0,
    val endOffset: Int = 0
)

// ── Place details response ────────────────────────────────────────────────────

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

// ── API error response ────────────────────────────────────────────────────────

@Serializable
internal data class GooglePlacesErrorResponse(
    val error: GooglePlacesErrorDetail? = null
)

@Serializable
internal data class GooglePlacesErrorDetail(
    val code: Int = 0,
    val message: String = "",
    val status: String = ""
)

@Serializable
internal class GooglePlacesApiError(
    val code: Int,
    val status: String,
    val apiMessage: String,
) : W3WError(message = "[$status $code] $apiMessage")

internal suspend fun HttpResponse.toGooglePlacesApiError(): GooglePlacesApiError {
    val detail = try {
        body<GooglePlacesErrorResponse>().error
    } catch (_: Exception) {
        null
    }
    return if (detail != null) {
        GooglePlacesApiError(
            code = detail.code,
            status = detail.status,
            apiMessage = detail.message,
        )
    } else {
        GooglePlacesApiError(
            code = status.value,
            status = status.description,
            apiMessage = "Unexpected API error (HTTP ${status.value})",
        )
    }
}