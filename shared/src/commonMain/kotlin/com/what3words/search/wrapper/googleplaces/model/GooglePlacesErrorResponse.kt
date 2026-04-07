package com.what3words.search.wrapper.googleplaces.model

import com.what3words.core.types.common.W3WError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.Serializable


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
internal data class GooglePlacesApiError(
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