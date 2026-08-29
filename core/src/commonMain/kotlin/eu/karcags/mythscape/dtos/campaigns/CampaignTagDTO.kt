package eu.karcags.mythscape.dtos.campaigns

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CampaignTagDTO(
    val id: Int,
    val caption: String,
    val color: String,
    val creation: LocalDateTime,
)

@Serializable
data class CampaignTagEditDTO(
    val caption: String,
    val color: String,
)