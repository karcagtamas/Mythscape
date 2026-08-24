package eu.karcags.mythscape.modules.application.dao

import eu.karcags.mythscape.modules.application.db.UsersTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable)

    var name by UsersTable.name
    var username by UsersTable.username
    var email by UsersTable.email
    var password by UsersTable.password
    var register by UsersTable.register
}