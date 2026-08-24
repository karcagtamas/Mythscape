package eu.karcags.mythscape.modules.note.dao

import eu.karcags.mythscape.modules.campaign.dao.CampaignEntity
import eu.karcags.mythscape.modules.campaign.dao.SessionEntity
import eu.karcags.mythscape.modules.note.db.FoldersTable
import eu.karcags.mythscape.modules.note.db.NotesTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class FolderEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FolderEntity>(FoldersTable)

    var name by FoldersTable.name
    var campaign by CampaignEntity referencedOn FoldersTable.campaign
    var parent by FolderEntity optionalReferencedOn FoldersTable.parent
    var category by NoteCategoryEntity optionalReferencedOn FoldersTable.category
    var session by SessionEntity optionalReferencedOn FoldersTable.session
    var creation by FoldersTable.creation
    var lastUpdate by FoldersTable.lastUpdate
    val notes by NoteEntity optionalReferrersOn NotesTable.folder
    val folders by FolderEntity optionalReferrersOn FoldersTable.parent
}