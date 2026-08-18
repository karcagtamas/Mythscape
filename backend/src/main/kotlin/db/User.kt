package eu.karcags.mythscape.db

import eu.karcags.mythscape.utils.current
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.datetime.datetime

object Users : IntIdTable("users") {
    val name = varchar("name", 80)
    val username = varchar("username", 24).uniqueIndex()
    val email = varchar("email", 120)
    val password = varchar("password", 255)
    val register = datetime("register").default(current())
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var name by Users.name
    var username by Users.username
    var email by Users.email
    var password by Users.password
    var register by Users.register
}