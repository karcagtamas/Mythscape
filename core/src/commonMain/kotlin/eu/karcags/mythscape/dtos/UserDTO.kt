package eu.karcags.mythscape.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
)