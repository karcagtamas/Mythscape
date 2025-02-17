package eu.karcags.mythscape.dtos

import kotlinx.serialization.Serializable

@Serializable
data class LoginDTO(val username: String, val password: String)
