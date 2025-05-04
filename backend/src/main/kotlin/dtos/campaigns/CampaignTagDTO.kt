package eu.karcags.mythscape.dtos.campaigns

import eu.karcags.mythscape.db.CampaignTag
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CampaignTagDTO(
    val id: Int,
    val caption: String,
    val color: String,
    val creation: LocalDateTime,
)

fun CampaignTag.campaignTagDTO(): CampaignTagDTO {
    return CampaignTagDTO(id.value, caption, color, creation)
}

fun List<CampaignTag>.campaignTagListDTO(): List<CampaignTagDTO> {
    return map { it.campaignTagDTO() }
}

@Serializable
data class CampaignTagEditDTO(
    val caption: String,
    val color: String,
)