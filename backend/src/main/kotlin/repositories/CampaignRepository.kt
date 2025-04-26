package eu.karcags.mythscape.repositories

import eu.karcags.mythscape.db.Campaign
import eu.karcags.mythscape.db.CampaignMember
import eu.karcags.mythscape.db.CampaignTag

interface CampaignRepository : Repository<Campaign> {

    suspend fun <U> byUserId(userId: Int, mapper: (Campaign) -> U): List<U>

    suspend fun <U> getTags(campaignId: Int, mapper: (CampaignTag) -> U): List<U>

    suspend fun <U> getMembers(campaignId: Int, mapper: (CampaignMember) -> U): List<U>
}