package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.modules.application.dao.UserEntity
import eu.karcags.mythscape.modules.application.db.UsersTable
import eu.karcags.mythscape.repositories.UserRepository
import eu.karcags.mythscape.utils.suspendTransaction
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.dao.IntEntityClass

class UserRepositoryImpl : RepositoryImpl<UserEntity>(), UserRepository {

    override fun entityClass(): IntEntityClass<UserEntity> = UserEntity

    override suspend fun findByUsername(username: String): UserEntity? = suspendTransaction {
        UserEntity.find { UsersTable.username eq username }.firstOrNull()
    }

    override suspend fun existsByUsernameOrEmail(username: String, email: String): Boolean = suspendTransaction {
        UserEntity.find { (UsersTable.username eq username) or (UsersTable.email eq email) }.any()
    }
}