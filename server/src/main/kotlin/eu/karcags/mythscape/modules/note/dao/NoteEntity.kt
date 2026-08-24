package eu.karcags.mythscape.modules.note.dao

import eu.karcags.mythscape.modules.note.db.NotesTable
import eu.karcags.mythscape.modules.application.dao.UserEntity
import eu.karcags.mythscape.modules.campaign.dao.CampaignEntity
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class NoteEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NoteEntity>(NotesTable)

    var name by NotesTable.name
    var creator by UserEntity referencedOn NotesTable.creator
    var campaign by CampaignEntity referencedOn NotesTable.campaign
    var folder by FolderEntity.Companion optionalReferencedOn NotesTable.folder
    var content by NotesTable.content
    var creation by NotesTable.creation
    var lastUpdate by NotesTable.lastUpdate
}