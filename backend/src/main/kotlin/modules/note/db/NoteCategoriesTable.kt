package eu.karcags.mythscape.modules.note.db

import eu.karcags.mythscape.modules.application.db.UsersTable
import eu.karcags.mythscape.utils.current
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.datetime

object NoteCategoriesTable : IntIdTable("note_categories") {

    val name = varchar("name", 40)
    val creation = datetime("creation").default(current())
    val creator = reference("creator_id", UsersTable)
    val lastUpdate = datetime("last_update").default(current())
    val lastUpdater = reference("last_updater_id", UsersTable)
    val color = varchar("color", 7)
}