package eu.karcags.mythscape.dtos.notes

import eu.karcags.mythscape.db.NoteCategory
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CampaignNoteCategoryDTO(val id: Int, val name: String, val creation: LocalDateTime, val lastUpdate: LocalDateTime, val color: String)

fun NoteCategory.noteCategoryDTO(): CampaignNoteCategoryDTO {
    return CampaignNoteCategoryDTO(
        id.value,
        name,
        creation,
        lastUpdate,
        color,
    )
}
