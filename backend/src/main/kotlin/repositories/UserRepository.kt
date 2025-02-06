package eu.karcags.mythscape.repositories

import eu.karcags.mythscape.models.User

interface UserRepository {
    suspend fun byName(userName: String): User?
    suspend fun add(user: User)
}