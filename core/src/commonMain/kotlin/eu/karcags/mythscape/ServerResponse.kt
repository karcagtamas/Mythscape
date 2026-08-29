package eu.karcags.mythscape

import kotlinx.serialization.Serializable

@Serializable
data class ServerResponse<T>(
    val data: T? = null,
    val statusCode: Int,
    val success: Boolean,
    val error: ErrorResponse? = null
)

@Serializable
data class ErrorResponse(
    val message: String?,
    val stackTrace: List<String> = emptyList(),
    val subMessages: List<String> = emptyList(),
)