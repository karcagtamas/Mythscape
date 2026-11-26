package eu.karcags.mythscape.core.dtos.campaigns

import eu.karcags.mythscape.core.dtos.UserDTO
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CampaignDTO(
    val id: Int,
    val name: String,
    val title: String,
    val imageId: Int?,
    val description: String?,
    val archived: Boolean,
    val creator: UserDTO,
    val creation: LocalDateTime,
    val lastUpdate: LocalDateTime,
    val tags: Long,
    val members: Long,
    val sessions: Long,
)

@Serializable
data class CampaignRequestDTO(
    val name: String,
    val title: String,
    val description: String?,
)

@Serializable
data class CampaignLightDTO(
    val id: Int,
    val name: String,
    val title: String,
)