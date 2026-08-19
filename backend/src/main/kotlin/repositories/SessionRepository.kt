package eu.karcags.mythscape.repositories

import eu.karcags.mythscape.modules.campaign.dao.SessionEntity

interface SessionRepository : Repository<SessionEntity> {

    suspend fun <U> byCampaign(campaignId: Int, mapper: (SessionEntity) -> U): List<U>

    suspend fun <U> query(campaignId: Int?, showAll: Boolean, mapper: (SessionEntity) -> U): List<U>
}