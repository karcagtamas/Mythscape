package eu.karcags.mythscape.dtos.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginDTO(val username: String, val password: String)
