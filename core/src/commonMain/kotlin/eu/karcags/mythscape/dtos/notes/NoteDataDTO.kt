package eu.karcags.mythscape.dtos.notes

import kotlinx.serialization.Serializable

@Serializable
data class NoteDataDTO(val id: Int, val content: String)