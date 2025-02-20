package eu.karcags.mythscape.repositories

import eu.karcags.mythscape.db.Campaign

interface CampaignRepository : Repository<Campaign> {

    suspend fun <U> byUserId(userId: Int, mapper: (Campaign) -> U): List<U>
}