package eu.karcags.mythscape.dtos.sessions

import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignLightDTO
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class SessionDTO(
    val id: Int,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val canceled: Boolean = false,
    val campaign: CampaignLightDTO
)

@Serializable
data class SessionRequestDTO(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val campaignId: Int,
    val canceled: Boolean = false,
)