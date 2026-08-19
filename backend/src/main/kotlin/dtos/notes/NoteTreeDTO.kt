package eu.karcags.mythscape.dtos.notes

import eu.karcags.mythscape.modules.campaign.dao.CampaignEntity
import eu.karcags.mythscape.modules.note.dao.FolderEntity
import eu.karcags.mythscape.modules.note.dao.NoteEntity
import kotlinx.serialization.Serializable

@Serializable
data class NoteTreeDTO(val key: Key, val name: String, val children: List<NoteTreeDTO>?) {

    @Serializable
    data class Key(val type: Type, val id: Int) {

        enum class Type {
            FOLDER,
            NOTE,
        }
    }
}

fun CampaignEntity.treeDTO(): List<NoteTreeDTO> {
    return folders.filter { folder -> folder.parent == null }.map { folder -> folder.folderTreeDTO() } + notes.filter { note ->  note.folder == null }.map { note -> note.noteTreeDTO() }
}

fun NoteEntity.noteTreeDTO(): NoteTreeDTO {
    return NoteTreeDTO(
        NoteTreeDTO.Key(NoteTreeDTO.Key.Type.NOTE, id.value),
        name,
        null,
    )
}

fun FolderEntity.folderTreeDTO(): NoteTreeDTO {
    return NoteTreeDTO(
        NoteTreeDTO.Key(NoteTreeDTO.Key.Type.FOLDER, id.value),
        name,
        folders.map { folder -> folder.folderTreeDTO() } + notes.map { note -> note.noteTreeDTO() }
    )
}