package eu.karcags.mythscape.modules.campaign.db

import eu.karcags.mythscape.utils.current
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.datetime

object CampaignTagsTable : IntIdTable("campaign_tags") {
    val campaign = reference("campaign_id", CampaignsTable, onDelete = ReferenceOption.CASCADE)
    val caption = varchar("caption", 40)
    val color = varchar("color", 7)
    val creation = datetime("creation").default(current())
}