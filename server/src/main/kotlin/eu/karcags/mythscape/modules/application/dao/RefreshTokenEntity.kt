package eu.karcags.mythscape.modules.application.dao

import eu.karcags.mythscape.modules.application.db.RefreshTokensTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class RefreshTokenEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RefreshTokenEntity>(RefreshTokensTable)

    var userId by RefreshTokensTable.userId
    var clientId by RefreshTokensTable.clientId
    var token by RefreshTokensTable.token
    var expiration by RefreshTokensTable.expiration
    var revoked by RefreshTokensTable.revoked
}