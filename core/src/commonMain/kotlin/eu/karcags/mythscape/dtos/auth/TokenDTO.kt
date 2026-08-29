package eu.karcags.mythscape.dtos.auth

import eu.karcags.mythscape.dtos.UserDTO
import kotlinx.serialization.Serializable

@Serializable
data class TokenDTO(
    val token: String,
    val expiresAt: Long,
    val user: UserDTO,
    val refreshToken: String,
    val clientId: String
)
