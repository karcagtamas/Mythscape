package eu.karcags.mythscape.dtos.notes

import eu.karcags.mythscape.modules.note.dao.FolderEntity
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class FolderDTO(
    val id: Int,
    val name: String,
    val folders: List<FolderDTO>,
    val notes: List<NoteDTO>,
    val categoryId: Int?,
    val sessionId: Int?,
    val creation: LocalDateTime,
    val lastUpdate: LocalDateTime,
)

fun FolderEntity.folderDTO(): FolderDTO {
    return FolderDTO(
        id.value,
        name,
        folders.toList().folderListDTO(),
        notes.toList().noteListDTO(),
        category?.id?.value,
        session?.id?.value,
        creation,
        lastUpdate,
    )
}

fun List<FolderEntity>.folderListDTO(): List<FolderDTO> {
    return map { it.folderDTO() }
}