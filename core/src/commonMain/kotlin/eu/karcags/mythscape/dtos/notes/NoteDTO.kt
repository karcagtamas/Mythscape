package eu.karcags.mythscape.dtos.notes

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