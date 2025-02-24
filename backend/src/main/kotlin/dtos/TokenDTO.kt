package eu.karcags.mythscape.dtos

import kotlinx.serialization.Serializable

@Serializable
data class TokenDTO(val token: String, val user: UserDTO)
