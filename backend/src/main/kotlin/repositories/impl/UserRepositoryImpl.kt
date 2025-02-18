package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.db.User
import eu.karcags.mythscape.db.Users
import eu.karcags.mythscape.repositories.UserRepository
import eu.karcags.mythscape.utils.suspendTransaction
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.or

class UserRepositoryImpl : RepositoryImpl<User>(), UserRepository {

    override fun entityClass(): IntEntityClass<User> = User

    override suspend fun findByUsername(username: String): User? = suspendTransaction {
        User.find { Users.username eq username }.firstOrNull()
    }

    override suspend fun existsByUsernameOrEmail(username: String, email: String): Boolean = suspendTransaction {
        User.find { (Users.username eq username) or (Users.email eq email) }.any()
    }
}