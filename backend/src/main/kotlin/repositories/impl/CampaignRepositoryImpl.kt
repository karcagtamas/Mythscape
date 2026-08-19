package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.modules.campaign.dao.CampaignEntity
import eu.karcags.mythscape.modules.campaign.dao.CampaignMemberEntity
import eu.karcags.mythscape.modules.campaign.dao.CampaignTagEntity
import eu.karcags.mythscape.modules.campaign.db.CampaignMembersTable
import eu.karcags.mythscape.modules.campaign.db.CampaignTagsTable
import eu.karcags.mythscape.modules.campaign.db.CampaignsTable
import eu.karcags.mythscape.repositories.CampaignRepository
import eu.karcags.mythscape.utils.dbQuery
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.IntEntityClass

class CampaignRepositoryImpl : RepositoryImpl<CampaignEntity>(), CampaignRepository {
    override fun entityClass(): IntEntityClass<CampaignEntity> = CampaignEntity

    override suspend fun <U> byUserId(userId: Int, mapper: (CampaignEntity) -> U): List<U> = dbQuery {
        CampaignEntity.find { CampaignsTable.creator eq userId }.toList().map { mapper(it) }
    }

    override suspend fun <U> getTags(campaignId: Int, mapper: (CampaignTagEntity) -> U): List<U> = dbQuery {
        CampaignTagEntity.find { CampaignTagsTable.campaign eq campaignId }.toList().map { mapper(it) }
    }

    override suspend fun <U> getMembers(campaignId: Int, mapper: (CampaignMemberEntity) -> U): List<U> = dbQuery {
        CampaignMemberEntity.find { CampaignMembersTable.campaign eq campaignId }.toList().map { mapper(it) }
    }

    override suspend fun createTag(fn: CampaignTagEntity.() -> Unit): Int = dbQuery {
        val result = CampaignTagEntity.new {
            this.apply(fn)
        }

        result.id.value
    }

    override suspend fun deleteTag(tagId: Int): Unit = dbQuery {
        CampaignTagEntity.findById(tagId)?.delete()
    }
}