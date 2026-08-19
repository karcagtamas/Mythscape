package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.modules.campaign.dao.SessionEntity
import eu.karcags.mythscape.modules.campaign.db.SessionsTable
import eu.karcags.mythscape.repositories.SessionRepository
import eu.karcags.mythscape.utils.currentDate
import eu.karcags.mythscape.utils.suspendTransaction
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.dao.IntEntityClass

class SessionRepositoryImpl : RepositoryImpl<SessionEntity>(), SessionRepository {
    override fun entityClass(): IntEntityClass<SessionEntity> = SessionEntity

    override suspend fun <U> byCampaign(campaignId: Int, mapper: (SessionEntity) -> U): List<U> = suspendTransaction {
        SessionEntity.find { SessionsTable.campaign eq campaignId }.toList().map { mapper(it) }
    }

    override suspend fun <U> query(campaignId: Int?, showAll: Boolean, mapper: (SessionEntity) -> U): List<U> = suspendTransaction {
        SessionEntity.find {
            val operations = mutableListOf<Op<Boolean>>()

            if (campaignId != null) {
                operations.add(SessionsTable.campaign eq campaignId)
            }

            if (!showAll) {
                operations.add(SessionsTable.date greaterEq currentDate())
            }

            operations.fold((SessionsTable.id greater 0) as Op<Boolean>) { acc, a ->
                acc and a
            }
        }.toList().map { mapper(it) }
    }
}