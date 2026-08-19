package eu.karcags.mythscape.repositories

import eu.karcags.mythscape.modules.application.dao.UserEntity

interface UserRepository : Repository<UserEntity> {
    suspend fun findByUsername(username: String): UserEntity?
    suspend fun existsByUsernameOrEmail(username: String, email: String): Boolean
}