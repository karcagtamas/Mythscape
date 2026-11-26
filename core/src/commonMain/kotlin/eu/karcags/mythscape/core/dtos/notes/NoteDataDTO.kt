package eu.karcags.mythscape.core.dtos.notes

import kotlinx.serialization.Serializable

@Serializable
data class NoteDataDTO(val id: Int, val content: String)