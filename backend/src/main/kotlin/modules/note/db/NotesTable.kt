package eu.karcags.mythscape.modules.note.db

import eu.karcags.mythscape.modules.application.db.UsersTable
import eu.karcags.mythscape.modules.campaign.db.CampaignsTable
import eu.karcags.mythscape.utils.current
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.datetime

object NotesTable : IntIdTable("notes") {
    val name = varchar("name", 120)
    val creator = reference("creator_id", UsersTable)
    val campaign = reference("campaign_id", CampaignsTable, onDelete = ReferenceOption.CASCADE)
    val folder = reference("folder_id", FoldersTable, onDelete = ReferenceOption.CASCADE).nullable()
    val content = text("content")
    val creation = datetime("creation").default(current())
    val lastUpdate = datetime("last_update").default(current())
}