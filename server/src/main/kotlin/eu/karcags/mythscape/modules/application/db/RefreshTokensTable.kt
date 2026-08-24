package eu.karcags.mythscape.modules.application.db

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.datetime

object RefreshTokensTable : IntIdTable("refresh_tokens") {
    val userId = integer("user_id")
    val clientId = varchar("client_id", 40)
    val token = varchar("token", 40)
    val expiration = datetime("expiration")
    val revoked = datetime("revoked").nullable()
}