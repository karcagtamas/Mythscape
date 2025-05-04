package eu.karcags.mythscape.db

import eu.karcags.mythscape.utils.current
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Folders : IntIdTable("folders") {
    val name = varchar("name", 256)
    val campaign = reference("campaign_id", Campaigns, onDelete = ReferenceOption.CASCADE)
    val parent = reference("parent_id", Folders, onDelete = ReferenceOption.CASCADE).nullable()
    val category = reference("category_id", NoteCategories, onDelete = ReferenceOption.SET_NULL).nullable()
    val session = reference("session_id", Sessions, onDelete = ReferenceOption.SET_NULL).nullable()
    val creation = datetime("creation").default(current())
    val lastUpdate = datetime("last_update").default(current())
}

class Folder(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Folder>(Folders)

    var name by Folders.name
    var campaign by Campaign referencedOn Folders.campaign
    var parent by Folder optionalReferencedOn Folders.parent
    var category by NoteCategory optionalReferencedOn Folders.category
    var session by Session optionalReferencedOn Folders.session
    var creation by Folders.creation
    var lastUpdate by Folders.lastUpdate
    val notes by Note optionalReferrersOn Notes.folder
    val folders by Folder optionalReferrersOn Folders.parent
}