package eu.karcags.mythscape.dtos.notes

import eu.karcags.mythscape.db.Note
import kotlinx.serialization.Serializable

@Serializable
data class NoteDataDTO(val id: Int, val content: String)

fun Note.noteDataDTO(): NoteDataDTO {
    return NoteDataDTO(id.value, content)
}