package eu.karcags.mythscape.core.dtos.auth

import eu.karcags.mythscape.core.dtos.UserDTO
import kotlinx.serialization.Serializable

@Serializable
data class TokenDTO(
    val token: String,
    val expiresAt: Long,
    val user: UserDTO,
    val refreshToken: String,
    val clientId: String
)
