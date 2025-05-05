package eu.karcags.mythscape.repositories

import eu.karcags.mythscape.db.Session

interface SessionRepository : Repository<Session> {

    suspend fun <U> byCampaign(campaignId: Int, mapper: (Session) -> U): List<U>
}