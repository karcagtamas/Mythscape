package eu.karcags.mythscape.dtos.notes

import eu.karcags.mythscape.modules.note.dao.NoteEntity
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class NoteDTO(
    val id: Int,
    val name: String,
    val folderId: Int?,
    val creation: LocalDateTime,
    val lastUpdate: LocalDateTime,
)

fun NoteEntity.noteDTO(): NoteDTO {
    return NoteDTO(
        id.value,
        name,
        folder?.id?.value,
        creation,
        lastUpdate,
    )
}

fun List<NoteEntity>.noteListDTO(): List<NoteDTO> {
    return map { it.noteDTO() }
}
