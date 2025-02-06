package eu.karcags.mythscape.repositories

import eu.karcags.mythscape.dtos.UserDTO

interface UserRepository {

    suspend fun all(): List<UserDTO>
    suspend fun get(id: Int): UserDTO?
}