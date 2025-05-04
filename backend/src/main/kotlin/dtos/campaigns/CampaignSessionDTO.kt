package eu.karcags.mythscape.dtos.campaigns

import eu.karcags.mythscape.db.Session
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class CampaignSessionDTO(val id: Int, val date: LocalDateTime, val startTime: LocalTime, val endTime: LocalTime)

fun Session.sessionDTO(): CampaignSessionDTO {
    return CampaignSessionDTO(
        id.value,
        date,
        startTime,
        endTime,
    )
}