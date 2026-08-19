package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.modules.application.dao.RefreshTokenEntity
import eu.karcags.mythscape.modules.application.db.RefreshTokensTable
import eu.karcags.mythscape.dtos.auth.RefreshDTO
import eu.karcags.mythscape.repositories.RefreshTokenRepository
import eu.karcags.mythscape.utils.current
import eu.karcags.mythscape.utils.suspendTransaction
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.dao.IntEntityClass

class RefreshTokenRepositoryImpl : RepositoryImpl<RefreshTokenEntity>(), RefreshTokenRepository {
    override fun entityClass(): IntEntityClass<RefreshTokenEntity> = RefreshTokenEntity

    override suspend fun find(dto: RefreshDTO): RefreshTokenEntity? = suspendTransaction {
        val now = current()
        RefreshTokenEntity.find {
            (RefreshTokensTable.userId eq dto.userId) and
                    (RefreshTokensTable.token eq dto.refreshToken) and
                    (RefreshTokensTable.clientId eq dto.clientId) and
                    (RefreshTokensTable.expiration greater now) and
                    (RefreshTokensTable.revoked.isNull())
        }
            .firstOrNull()
    }

    override suspend fun revokeAll(userId: Int, clientId: String): Unit = suspendTransaction {
        RefreshTokenEntity.find { (RefreshTokensTable.clientId eq clientId) and (RefreshTokensTable.userId eq userId) }
            .forUpdate()
            .forEach {
                it.revoked = current()
            }
    }
}