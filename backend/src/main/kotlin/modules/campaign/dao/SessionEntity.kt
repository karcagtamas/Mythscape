package eu.karcags.mythscape.modules.campaign.dao

import eu.karcags.mythscape.modules.campaign.db.SessionsTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class SessionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SessionEntity>(SessionsTable)

    var date by SessionsTable.date
    var startTime by SessionsTable.startTime
    var endTime by SessionsTable.endTime
    var campaign by CampaignEntity referencedOn SessionsTable.campaign
}