package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.db.Campaign
import eu.karcags.mythscape.db.Campaigns
import eu.karcags.mythscape.repositories.CampaignRepository
import eu.karcags.mythscape.utils.suspendTransaction
import org.jetbrains.exposed.dao.IntEntityClass

class CampaignRepositoryImpl : RepositoryImpl<Campaign>(), CampaignRepository {
    override fun entityClass(): IntEntityClass<Campaign> = Campaign

    override suspend fun <U> byUserId(userId: Int, mapper: (Campaign) -> U): List<U> = suspendTransaction {
        Campaign.find { Campaigns.creator eq userId }.toList().map { mapper(it) }
    }
}