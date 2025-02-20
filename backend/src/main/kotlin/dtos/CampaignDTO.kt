package eu.karcags.mythscape.dtos

import eu.karcags.mythscape.db.Campaign
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CampaignDTO(
    val id: Int,
    val name: String,
    val description: String?,
    val creator: UserDTO,
    val creation: LocalDateTime,
    val lastUpdate: LocalDateTime,
)

fun Campaign.dto(): CampaignDTO {
    return CampaignDTO(id.value, name, description, creator.dto(), creation, lastUpdate)
}

fun List<Campaign>.dto(): List<CampaignDTO> {
    return map { it.dto() }
}

@Serializable
data class CampaignEditDTO(
    val name: String,
    val description: String?,
)