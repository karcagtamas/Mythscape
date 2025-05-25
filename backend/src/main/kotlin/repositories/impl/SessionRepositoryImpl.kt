package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.db.Session
import eu.karcags.mythscape.db.Sessions
import eu.karcags.mythscape.repositories.SessionRepository
import eu.karcags.mythscape.utils.currentDate
import eu.karcags.mythscape.utils.suspendTransaction
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and

class SessionRepositoryImpl : RepositoryImpl<Session>(), SessionRepository {
    override fun entityClass(): IntEntityClass<Session> = Session

    override suspend fun <U> byCampaign(campaignId: Int, mapper: (Session) -> U): List<U> = suspendTransaction {
        Session.find { Sessions.campaign eq campaignId }.toList().map { mapper(it) }
    }

    override suspend fun <U> query(campaignId: Int?, showAll: Boolean, mapper: (Session) -> U): List<U> = suspendTransaction {
        Session.find {
            val operations = mutableListOf<Op<Boolean>>()

            if (campaignId != null) {
                operations.add(Sessions.campaign eq campaignId)
            }

            if (!showAll) {
                operations.add(Sessions.date greaterEq currentDate())
            }

            operations.fold((Sessions.id greater 0) as Op<Boolean>) { acc, a ->
                acc and a
            }
        }.toList().map { mapper(it) }
    }
}