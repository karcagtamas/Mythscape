package eu.karcags.mythscape.repositories

import eu.karcags.mythscape.db.User

interface UserRepository : Repository<User> {
    suspend fun findByUsername(username: String): User?
}