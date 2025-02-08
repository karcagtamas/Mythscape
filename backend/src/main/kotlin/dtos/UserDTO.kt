package eu.karcags.mythscape.dtos

import eu.karcags.mythscape.db.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
)

fun User.dto(): UserDTO {
    return UserDTO(id.value, name, userName, email)
}

fun List<User>.dto(): List<UserDTO> {
    return map { it.dto() }
}