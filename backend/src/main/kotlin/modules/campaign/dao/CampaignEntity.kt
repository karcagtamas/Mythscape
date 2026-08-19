package eu.karcags.mythscape.modules.campaign.dao

import eu.karcags.mythscape.modules.note.dao.FolderEntity
import eu.karcags.mythscape.modules.note.dao.NoteEntity
import eu.karcags.mythscape.modules.campaign.db.CampaignMembersTable
import eu.karcags.mythscape.modules.campaign.db.CampaignTagsTable
import eu.karcags.mythscape.modules.note.db.FoldersTable
import eu.karcags.mythscape.modules.note.db.NotesTable
import eu.karcags.mythscape.modules.campaign.db.SessionsTable
import eu.karcags.mythscape.modules.campaign.db.CampaignsTable
import eu.karcags.mythscape.modules.application.dao.UserEntity
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class CampaignEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CampaignEntity>(CampaignsTable)

    var name by CampaignsTable.name
    var title by CampaignsTable.title
    var imageId by CampaignsTable.imageId
    var description by CampaignsTable.description
    var creator by UserEntity.Companion referencedOn CampaignsTable.creator
    var creation by CampaignsTable.creation
    var lastUpdate by CampaignsTable.lastUpdate
    val members by CampaignMemberEntity.Companion referrersOn CampaignMembersTable.campaign
    val tags by CampaignTagEntity.Companion referrersOn CampaignTagsTable.campaign
    val folders by FolderEntity.Companion referrersOn FoldersTable.campaign
    val notes by NoteEntity.Companion referrersOn NotesTable.campaign
    val sessions by SessionEntity.Companion referrersOn SessionsTable.campaign
}