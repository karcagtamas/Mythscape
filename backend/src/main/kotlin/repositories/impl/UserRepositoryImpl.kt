package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.db.User
import eu.karcags.mythscape.dtos.UserDTO
import eu.karcags.mythscape.dtos.dto
import eu.karcags.mythscape.repositories.UserRepository
import eu.karcags.mythscape.utils.suspendTransaction

class UserRepositoryImpl : UserRepository {
    override suspend fun all(): List<UserDTO> = suspendTransaction {
        User.all().map(::dto)
    }

    override suspend fun get(id: Int): UserDTO? = suspendTransaction {
        User.findById(id)?.let { dto(it) }
    }

}