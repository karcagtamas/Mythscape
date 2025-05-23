package eu.karcags.mythscape.dtos.sessions

import eu.karcags.mythscape.db.Session
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class SessionDTO(val id: Int, val date: LocalDateTime, val startTime: LocalTime, val endTime: LocalTime)

fun Session.sessionDTO(): SessionDTO {
    return SessionDTO(
        id.value,
        date,
        startTime,
        endTime,
    )
}