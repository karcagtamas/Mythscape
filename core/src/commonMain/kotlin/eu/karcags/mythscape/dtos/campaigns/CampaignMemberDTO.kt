package eu.karcags.mythscape.dtos.campaigns

import eu.karcags.mythscape.dtos.UserDTO
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
