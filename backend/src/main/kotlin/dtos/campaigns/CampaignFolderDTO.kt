package eu.karcags.mythscape.dtos.campaigns

import eu.karcags.mythscape.db.Folder
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CampaignFolderDTO(
    val id: Int,
    val name: String,
    val folders: List<CampaignFolderDTO>,
    val notes: List<CampaignNoteDTO>,
    val categoryId: Int?,
    val sessionId: Int?,
    val creation: LocalDateTime,
    val lastUpdate: LocalDateTime,
)

fun Folder.folderDTO(): CampaignFolderDTO {
    return CampaignFolderDTO(
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

fun List<Folder>.folderListDTO(): List<CampaignFolderDTO> {
    return map { it.folderDTO() }
}