package eu.karcags.mythscape.dtos.campaigns

import eu.karcags.mythscape.modules.campaign.dao.CampaignMemberEntity
import eu.karcags.mythscape.dtos.UserDTO
import eu.karcags.mythscape.dtos.dto
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CampaignMemberDTO(
    val id: Int,
    val name: String,
    val campaignId: Int,
    val user: UserDTO?,
    val creation: LocalDateTime,
    val isDM: Boolean,
)

fun CampaignMemberEntity.campaignMemberDTO(): CampaignMemberDTO {
    return CampaignMemberDTO(
        id.value,
        name,
        campaign.id.value,
        user?.dto(),
        creation,
        isDM,
    )
}

fun List<CampaignMemberEntity>.campaignMemberListDTO(): List<CampaignMemberDTO> {
    return map { it.campaignMemberDTO() }
}
