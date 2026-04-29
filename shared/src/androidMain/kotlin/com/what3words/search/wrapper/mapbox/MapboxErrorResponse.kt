package com.what3words.search.wrapper.mapbox

import com.what3words.core.types.common.W3WError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MapboxErrorResponse(
    val message: Message
)

@Serializable
data class Message(
    @SerialName("error")
    val error: String,
)

@Serializable
internal data class MapboxApiError(
    val httpStatus: Int,
    val apiMessage: String,
) : W3WError(message = "[HTTP $httpStatus] $apiMessage")

internal suspend fun HttpResponse.toMapboxApiError(): MapboxApiError {
    val message = try {
        body<MapboxErrorResponse>().message.error
    } catch (_: Exception) {
        null
    }
    return MapboxApiError(
        httpStatus = status.value,
        apiMessage = message ?: "Unexpected API error (HTTP ${status.value})",
    )
}
