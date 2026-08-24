package eu.karcags.mythscape.modules.application.services

import eu.karcags.mythscape.modules.application.dao.RefreshTokenEntity
import eu.karcags.mythscape.dtos.auth.RefreshDTO

interface RefreshTokenService {
    fun find(dto: RefreshDTO): RefreshTokenEntity?

    fun revokeAll(userId: Int, clientId: String)
}