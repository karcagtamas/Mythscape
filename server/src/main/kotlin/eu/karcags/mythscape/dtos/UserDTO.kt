package eu.karcags.mythscape.dtos

import eu.karcags.mythscape.modules.application.dao.UserEntity
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
)

fun UserEntity.dto(): UserDTO {
    return UserDTO(id.value, name, username, email)
}

fun List<UserEntity>.dto(): List<UserDTO> {
    return map { it.dto() }
}