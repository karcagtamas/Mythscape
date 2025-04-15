package eu.karcags.mythscape.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object RefreshTokens : IntIdTable("refresh_tokens") {
    val userId = integer("user_id")
    val clientId = varchar("client_id", 40)
    val token = varchar("token", 40)
    val expiration = datetime("expiration")
    val revoked = datetime("revoked").nullable()
}

class RefreshToken(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RefreshToken>(RefreshTokens)

    var userId by RefreshTokens.userId
    var clientId by RefreshTokens.clientId
    var token by RefreshTokens.token
    var expiration by RefreshTokens.expiration
    var revoked by RefreshTokens.revoked
}