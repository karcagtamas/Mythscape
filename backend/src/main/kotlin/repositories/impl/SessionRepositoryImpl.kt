package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.db.Session
import eu.karcags.mythscape.db.Sessions
import eu.karcags.mythscape.repositories.SessionRepository
import eu.karcags.mythscape.utils.suspendTransaction
import org.jetbrains.exposed.dao.IntEntityClass

class SessionRepositoryImpl : RepositoryImpl<Session>(), SessionRepository {
    override fun entityClass(): IntEntityClass<Session> = Session

    override suspend fun <U> byCampaign(campaignId: Int, mapper: (Session) -> U): List<U> = suspendTransaction {
        Session.find { Sessions.campaign eq campaignId }.toList().map { mapper(it) }
    }
}