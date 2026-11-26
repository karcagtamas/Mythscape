package eu.karcags.mythscape.core.dtos.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshDTO(val refreshToken: String, val clientId: String, val userId: Int)
