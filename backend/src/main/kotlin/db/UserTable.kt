package eu.karcags.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable("task") {
    val name = varchar("name", 120)
    val userName = varchar("userName", 24)
    val email = varchar("email", 40)
}

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UserTable)

    var name by UserTable.name
    var userName by UserTable.userName
    var email by UserTable.email
}