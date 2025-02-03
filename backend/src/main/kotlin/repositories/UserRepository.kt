package eu.karcags.repositories

import eu.karcags.models.User

interface UserRepository {
    suspend fun byName(userName: String): User?
    suspend fun add(user: User)
}