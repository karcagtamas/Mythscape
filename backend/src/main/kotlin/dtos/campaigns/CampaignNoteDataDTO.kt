package eu.karcags.mythscape.dtos.campaigns

import eu.karcags.mythscape.db.Note
import kotlinx.serialization.Serializable

@Serializable
data class CampaignNoteDataDTO(val id: Int, val content: String)

fun Note.noteDataDTO(): CampaignNoteDataDTO {
    return CampaignNoteDataDTO(id.value, content)
}