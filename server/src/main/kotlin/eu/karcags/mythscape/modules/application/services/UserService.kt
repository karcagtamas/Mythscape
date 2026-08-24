package eu.karcags.mythscape.modules.application.services

import eu.karcags.mythscape.modules.application.dao.UserEntity

interface UserService {

    fun getByName(name: String): UserEntity?

    fun existsByUsernameOrEmail(name: String, email: String): Boolean
}