package eu.karcags.mythscape.dtos.campaigns

import eu.karcags.mythscape.modules.campaign.dao.CampaignTagEntity
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CampaignTagDTO(
    val id: Int,
    val caption: String,
    val color: String,
    val creation: LocalDateTime,
)

fun CampaignTagEntity.campaignTagDTO(): CampaignTagDTO {
    return CampaignTagDTO(id.value, caption, color, creation)
}

fun List<CampaignTagEntity>.campaignTagListDTO(): List<CampaignTagDTO> {
    return map { it.campaignTagDTO() }
}

@Serializable
data class CampaignTagEditDTO(
    val caption: String,
    val color: String,
)