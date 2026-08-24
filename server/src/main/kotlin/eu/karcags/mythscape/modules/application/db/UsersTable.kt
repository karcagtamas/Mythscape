package eu.karcags.mythscape.modules.application.db

import eu.karcags.mythscape.utils.current
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.datetime

object UsersTable : IntIdTable("users") {
    val name = varchar("name", 80)
    val username = varchar("username", 24).uniqueIndex()
    val email = varchar("email", 120)
    val password = varchar("password", 255)
    val register = datetime("register").default(current())
}