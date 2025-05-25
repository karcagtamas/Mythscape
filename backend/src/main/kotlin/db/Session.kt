package eu.karcags.mythscape.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.time

object Sessions : IntIdTable("sessions") {

    val date = date(name = "date")
    val startTime = time("start_time")
    val endTime = time("end_time")
    val campaign = reference("campaign_id", Campaigns, onDelete = ReferenceOption.CASCADE)
}

class Session(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Session>(Sessions)

    var date by Sessions.date
    var startTime by Sessions.startTime
    var endTime by Sessions.endTime
    var campaign by Campaign referencedOn Sessions.campaign
}