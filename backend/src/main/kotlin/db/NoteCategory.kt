package eu.karcags.mythscape.db

import eu.karcags.mythscape.utils.current
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.datetime.datetime

object NoteCategories : IntIdTable("note_categories") {

    val name = varchar("name", 40)
    val creation = datetime("creation").default(current())
    val creator = reference("creator_id", Users)
    val lastUpdate = datetime("last_update").default(current())
    val lastUpdater = reference("last_updater_id", Users)
    val color = varchar("color", 7)
}

class NoteCategory(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NoteCategory>(NoteCategories)

    var name by NoteCategories.name
    var creation by NoteCategories.creation
    var creator by User referencedOn NoteCategories.creator
    var lastUpdate by NoteCategories.lastUpdate
    var lastUpdater by User referencedOn NoteCategories.lastUpdater
    var color by NoteCategories.color
}