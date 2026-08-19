package eu.karcags.mythscape.modules.application.services

import eu.karcags.mythscape.dtos.auth.RefreshDTO
import eu.karcags.mythscape.modules.application.dao.RefreshTokenEntity
import eu.karcags.mythscape.modules.application.db.RefreshTokensTable
import eu.karcags.mythscape.utils.current
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.isNull

class RefreshTokenServiceImpl : RefreshTokenService {

    override fun find(dto: RefreshDTO): RefreshTokenEntity? {
        val now = current()
        return RefreshTokenEntity.find {
            (RefreshTokensTable.userId eq dto.userId) and
                    (RefreshTokensTable.token eq dto.refreshToken) and
                    (RefreshTokensTable.clientId eq dto.clientId) and
                    (RefreshTokensTable.expiration greater now) and
                    (RefreshTokensTable.revoked.isNull())
        }
            .firstOrNull()
    }

    override fun revokeAll(userId: Int, clientId: String) {
        RefreshTokenEntity.find { (RefreshTokensTable.clientId eq clientId) and (RefreshTokensTable.userId eq userId) }
            .forUpdate()
            .forEach {
                it.revoked = current()
            }
    }
}