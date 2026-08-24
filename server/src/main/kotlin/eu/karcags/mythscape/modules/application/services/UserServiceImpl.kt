package eu.karcags.mythscape.modules.application.services

import eu.karcags.mythscape.modules.application.dao.UserEntity
import eu.karcags.mythscape.modules.application.db.UsersTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.or

class UserServiceImpl : UserService {

    override fun getByName(name: String): UserEntity? {
        return UserEntity.find { UsersTable.username eq name }.firstOrNull()
    }

    override fun existsByUsernameOrEmail(name: String, email: String): Boolean {
        return UserEntity.find { (UsersTable.username eq name) or (UsersTable.email eq email) }.empty().not()
    }
}