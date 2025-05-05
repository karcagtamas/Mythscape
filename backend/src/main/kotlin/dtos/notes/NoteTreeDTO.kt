package eu.karcags.mythscape.dtos.notes

import eu.karcags.mythscape.db.Campaign
import kotlinx.serialization.Serializable

@Serializable
data class NoteTreeDTO(val campaignId: Int, val notes: List<NoteDTO>, val folders: List<FolderDTO>)

fun Campaign.noteTreeDTO(): NoteTreeDTO {
    return NoteTreeDTO(
        id.value,
        notes.toList().noteListDTO(),
        folders.toList().folderListDTO(),
    )
}