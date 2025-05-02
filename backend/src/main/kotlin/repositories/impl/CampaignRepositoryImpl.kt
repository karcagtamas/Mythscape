package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.db.Campaign
import eu.karcags.mythscape.db.CampaignMember
import eu.karcags.mythscape.db.CampaignMembers
import eu.karcags.mythscape.db.CampaignTag
import eu.karcags.mythscape.db.CampaignTags
import eu.karcags.mythscape.db.Campaigns
import eu.karcags.mythscape.repositories.CampaignRepository
import eu.karcags.mythscape.utils.suspendTransaction
import org.jetbrains.exposed.dao.IntEntityClass

class CampaignRepositoryImpl : RepositoryImpl<Campaign>(), CampaignRepository {
    override fun entityClass(): IntEntityClass<Campaign> = Campaign

    override suspend fun <U> byUserId(userId: Int, mapper: (Campaign) -> U): List<U> = suspendTransaction {
        Campaign.find { Campaigns.creator eq userId }.toList().map { mapper(it) }
    }

    override suspend fun <U> getTags(campaignId: Int, mapper: (CampaignTag) -> U): List<U> = suspendTransaction {
        CampaignTag.find { CampaignTags.campaign eq campaignId }.toList().map { mapper(it) }
    }

    override suspend fun <U> getMembers(campaignId: Int, mapper: (CampaignMember) -> U): List<U> = suspendTransaction {
        CampaignMember.find { CampaignMembers.campaign eq campaignId }.toList().map { mapper(it) }
    }

    override suspend fun createTag(fn: CampaignTag.() -> Unit): Int = suspendTransaction {
        val result = CampaignTag.new {
            this.apply(fn)
        }

        result.id.value
    }

    override suspend fun deleteTag(tagId: Int) {
        CampaignTag.findById(tagId)?.delete()
    }
}