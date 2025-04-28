package eu.karcags.mythscape.dtos.campaigns

import eu.karcags.mythscape.db.Note
import kotlinx.serialization.Serializable

@Serializable
data class CampaignNoteDTO(
    val id: Int,
)

fun Note.dto(): CampaignNoteDTO {
    return CampaignNoteDTO(id.value)
}
