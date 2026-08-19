package eu.karcags.mythscape.repositories

import eu.karcags.mythscape.modules.application.dao.RefreshTokenEntity
import eu.karcags.mythscape.dtos.auth.RefreshDTO

interface RefreshTokenRepository : Repository<RefreshTokenEntity> {
    suspend fun find(dto: RefreshDTO): RefreshTokenEntity?

    suspend fun revokeAll(userId: Int, clientId: String)
}