package eu.karcags.mythscape.db

import eu.karcags.mythscape.utils.current
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.kotlin.datetime.time

object Sessions : IntIdTable("sessions") {

    val date = datetime(name = "date").default(current())
    val startTime = time("start_time")
    val endTime = time("end_time")
    val campaign = reference("campaign_id", Campaigns)
}

class Session(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Session>(Sessions)

    var date by Sessions.date
    val startTime by Sessions.startTime
    val endTime by Sessions.endTime
    var campaign by Campaign referencedOn Sessions.campaign
}