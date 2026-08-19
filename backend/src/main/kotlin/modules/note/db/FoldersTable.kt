package eu.karcags.mythscape.modules.note.db

import eu.karcags.mythscape.modules.campaign.db.CampaignsTable
import eu.karcags.mythscape.modules.campaign.db.SessionsTable
import eu.karcags.mythscape.utils.current
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.datetime

object FoldersTable : IntIdTable("folders") {
    val name = varchar("name", 256)
    val campaign = reference("campaign_id", CampaignsTable, onDelete = ReferenceOption.CASCADE)
    val parent = reference("parent_id", FoldersTable, onDelete = ReferenceOption.CASCADE).nullable()
    val category = reference("category_id", NoteCategoriesTable, onDelete = ReferenceOption.SET_NULL).nullable()
    val session = reference("session_id", SessionsTable, onDelete = ReferenceOption.SET_NULL).nullable()
    val creation = datetime("creation").default(current())
    val lastUpdate = datetime("last_update").default(current())
}