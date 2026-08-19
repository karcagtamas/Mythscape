package eu.karcags.mythscape.dtos.notes

import eu.karcags.mythscape.modules.note.dao.NoteEntity
import kotlinx.serialization.Serializable

@Serializable
data class NoteDataDTO(val id: Int, val content: String)

fun NoteEntity.noteDataDTO(): NoteDataDTO {
    return NoteDataDTO(id.value, content)
}