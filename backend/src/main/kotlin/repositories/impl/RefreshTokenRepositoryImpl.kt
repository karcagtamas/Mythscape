package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.db.RefreshToken
import eu.karcags.mythscape.db.RefreshTokens
import eu.karcags.mythscape.dtos.auth.RefreshDTO
import eu.karcags.mythscape.repositories.RefreshTokenRepository
import eu.karcags.mythscape.utils.current
import eu.karcags.mythscape.utils.suspendTransaction
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.dao.IntEntityClass

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