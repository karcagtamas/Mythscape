package eu.karcags.mythscape.modules.campaign.db

import eu.karcags.mythscape.modules.application.db.UsersTable
import eu.karcags.mythscape.utils.current
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.datetime

object CampaignsTable : IntIdTable("campaigns") {

    val name = varchar("name", 40)
    val title = varchar("title", 120)
    val imageId = integer("image_id").nullable()
    val description = text("description").nullable()
    val creator = reference("creator_id", UsersTable, onDelete = ReferenceOption.CASCADE)
    val creation = datetime("creation").default(current())
    val lastUpdate = datetime("last_update").default(current())
}