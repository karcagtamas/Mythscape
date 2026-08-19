package eu.karcags.mythscape.modules.campaign.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.time

object SessionsTable : IntIdTable("sessions") {

    val date = date(name = "date")
    val startTime = time("start_time")
    val endTime = time("end_time")
    val campaign = reference("campaign_id", CampaignsTable, onDelete = ReferenceOption.CASCADE)
}