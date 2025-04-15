package eu.karcags.mythscape.dtos.auth

import eu.karcags.mythscape.dtos.UserDTO
import kotlinx.serialization.Serializable

@Serializable
data class TokenDTO(val token: String, val user: UserDTO, val refreshToken: String, val clientId: String)
