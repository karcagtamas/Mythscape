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

fun dto(user: User): UserDTO {
    return UserDTO(user.id.value, user.name, user.userName, user.email)
}