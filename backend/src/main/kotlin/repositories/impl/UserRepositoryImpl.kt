package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.db.User
import eu.karcags.mythscape.repositories.UserRepository
import org.jetbrains.exposed.dao.IntEntityClass

class UserRepositoryImpl : RepositoryImpl<User>(), UserRepository {

    override fun entityClass(): IntEntityClass<User> = User
}