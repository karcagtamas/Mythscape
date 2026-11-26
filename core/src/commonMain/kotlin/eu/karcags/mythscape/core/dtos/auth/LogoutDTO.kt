package eu.karcags.mythscape.core.dtos.auth

import kotlinx.serialization.Serializable

@Serializable
data class LogoutDTO(val userId: Int, val clientId: String)
