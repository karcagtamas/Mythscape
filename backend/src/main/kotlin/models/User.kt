package eu.karcags.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val userName: String,
    val email: String,
)