package eu.karcags.mythscape.dtos.sessions

import eu.karcags.mythscape.db.Session
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class SessionDTO(val id: Int, val date: LocalDate, val startTime: LocalTime, val endTime: LocalTime)

fun Session.sessionDTO(): SessionDTO {
    return SessionDTO(
        id.value,
        date,
        startTime,
        endTime,
    )
}

@Serializable
data class SessionEditDTO(val date: LocalDate, val startTime: LocalTime, val endTime: LocalTime, val campaignId: Int)