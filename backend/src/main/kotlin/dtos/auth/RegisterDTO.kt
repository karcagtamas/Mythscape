package eu.karcags.mythscape.dtos.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDTO(val username: String, val password: String, val passwordConfirm: String, val fullname: String, val email: String)
