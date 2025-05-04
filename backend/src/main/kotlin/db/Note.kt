package eu.karcags.mythscape.db

import eu.karcags.mythscape.utils.current
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Notes : IntIdTable("notes") {
    val name = varchar("name", 120)
    val creator = reference("creator_id", Users)
    val campaign = reference("campaign_id", Campaigns, onDelete = ReferenceOption.CASCADE)
    val folder = reference("folder_id", Folders, onDelete = ReferenceOption.CASCADE).nullable()
    val content = text("content")
    val creation = datetime("creation").default(current())
    val lastUpdate = datetime("last_update").default(current())
}

class Note(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Note>(Notes)

    var name by Notes.name
    var creator by User referencedOn Notes.creator
    var campaign by Campaign referencedOn Notes.campaign
    var folder by Folder optionalReferencedOn Notes.folder
    var content by Notes.content
    var creation by Notes.creation
    var lastUpdate by Notes.lastUpdate
}