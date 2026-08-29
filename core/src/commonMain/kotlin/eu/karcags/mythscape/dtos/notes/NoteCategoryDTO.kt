package eu.karcags.mythscape.dtos.notes

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CampaignNoteCategoryDTO(
    val id: Int,
    val name: String,
    val creation: LocalDateTime,
    val lastUpdate: LocalDateTime,
    val color: String
)