package eu.karcags.mythscape.dtos.campaigns

import eu.karcags.mythscape.db.Note
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CampaignNoteDTO(
    val id: Int,
    val name: String,
    val folderId: Int?,
    val creation: LocalDateTime,
    val lastUpdate: LocalDateTime,
)

fun Note.noteDTO(): CampaignNoteDTO {
    return CampaignNoteDTO(
        id.value,
        name,
        folder?.id?.value,
        creation,
        lastUpdate,
    )
}

fun List<Note>.noteListDTO(): List<CampaignNoteDTO> {
    return map { it.noteDTO() }
}
