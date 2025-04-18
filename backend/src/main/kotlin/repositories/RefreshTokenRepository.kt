package eu.karcags.mythscape.repositories

import eu.karcags.mythscape.db.RefreshToken
import eu.karcags.mythscape.dtos.auth.RefreshDTO

interface RefreshTokenRepository : Repository<RefreshToken> {
    suspend fun find(dto: RefreshDTO): RefreshToken?

    suspend fun revokeAll(userId: Int, clientId: String)
}