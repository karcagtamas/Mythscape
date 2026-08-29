package eu.karcags.mythscape.dtos.sessions

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class SessionDTO(val id: Int, val date: LocalDate, val startTime: LocalTime, val endTime: LocalTime)

@Serializable
data class SessionEditDTO(val date: LocalDate, val startTime: LocalTime, val endTime: LocalTime, val campaignId: Int)