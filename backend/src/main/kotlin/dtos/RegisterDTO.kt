package eu.karcags.mythscape.dtos

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDTO(val username: String, val password: String, val fullname: String, val email: String)
