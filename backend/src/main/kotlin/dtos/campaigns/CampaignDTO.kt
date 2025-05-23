package eu.karcags.mythscape.dtos.campaigns

import eu.karcags.mythscape.db.Campaign
import eu.karcags.mythscape.dtos.UserDTO
import eu.karcags.mythscape.dtos.dto
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CampaignDTO(
    val id: Int,
    val name: String,
    val title: String,
    val imageId: Int?,
    val description: String?,
    val creator: UserDTO,
    val creation: LocalDateTime,
    val lastUpdate: LocalDateTime
)

fun Campaign.campaignDTO(): CampaignDTO {
    return CampaignDTO(
        id.value,
        name,
        title,
        imageId,
        description,
        creator.dto(),
        creation,
        lastUpdate,
    )
}

fun List<Campaign>.campaignListDTO(): List<CampaignDTO> {
    return map { it.campaignDTO() }
}

@Serializable
data class CampaignEditDTO(
    val name: String,
    val title: String,
    val description: String?,
)