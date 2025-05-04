package eu.karcags.mythscape.dtos.campaigns

import eu.karcags.mythscape.db.Campaign
import kotlinx.serialization.Serializable

@Serializable
data class CampaignNoteTreeDTO(val campaignId: Int, val notes: List<CampaignNoteDTO>, val folders: List<CampaignFolderDTO>)

fun Campaign.noteTreeDTO(): CampaignNoteTreeDTO {
    return CampaignNoteTreeDTO(
        id.value,
        notes.toList().noteListDTO(),
        folders.toList().folderListDTO(),
    )
}