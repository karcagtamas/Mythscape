package eu.karcags.mythscape.repositories

import eu.karcags.mythscape.modules.campaign.dao.CampaignEntity
import eu.karcags.mythscape.modules.campaign.dao.CampaignMemberEntity
import eu.karcags.mythscape.modules.campaign.dao.CampaignTagEntity

interface CampaignRepository : Repository<CampaignEntity> {

    suspend fun <U> byUserId(userId: Int, mapper: (CampaignEntity) -> U): List<U>

    suspend fun <U> getTags(campaignId: Int, mapper: (CampaignTagEntity) -> U): List<U>

    suspend fun <U> getMembers(campaignId: Int, mapper: (CampaignMemberEntity) -> U): List<U>

    suspend fun createTag(fn: CampaignTagEntity.() -> Unit): Int

    suspend fun deleteTag(tagId: Int)
}