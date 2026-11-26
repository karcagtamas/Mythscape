package eu.karcags.mythscape.core.dtos.campaigns

import eu.karcags.mythscape.core.dtos.UserDTO
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

@Serializable
data class CampaignMemberRequestDTO(
    val name: String,
    val userId: Int?,
)