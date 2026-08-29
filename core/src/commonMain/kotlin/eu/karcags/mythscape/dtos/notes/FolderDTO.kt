package eu.karcags.mythscape.dtos.notes

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