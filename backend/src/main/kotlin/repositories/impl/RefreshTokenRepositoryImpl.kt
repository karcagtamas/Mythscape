package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.db.RefreshToken
import eu.karcags.mythscape.db.RefreshTokens
import eu.karcags.mythscape.dtos.auth.RefreshDTO
import eu.karcags.mythscape.repositories.RefreshTokenRepository
import eu.karcags.mythscape.utils.current
import eu.karcags.mythscape.utils.suspendTransaction
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.update

class RefreshTokenRepositoryImpl : RepositoryImpl<RefreshToken>(), RefreshTokenRepository {
    override fun entityClass(): IntEntityClass<RefreshToken> = RefreshToken

    override suspend fun find(dto: RefreshDTO): RefreshToken? = suspendTransaction {
        val now = current()
        RefreshToken.find {
            (RefreshTokens.userId eq dto.userId) and
                    (RefreshTokens.token eq dto.refreshToken) and
                    (RefreshTokens.clientId eq dto.clientId) and
                    (RefreshTokens.expiration greater now) and
                    (RefreshTokens.revoked.isNull())
        }
            .firstOrNull()
    }

    override suspend fun revokeAll(userId: Int, clientId: String): Unit = suspendTransaction {
        RefreshToken.find { (RefreshTokens.clientId eq clientId) and (RefreshTokens.userId eq userId) }
            .forUpdate()
            .forEach {
                it.revoked = current()
            }
    }
}