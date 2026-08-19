package eu.karcags.mythscape.modules.campaign.db

import eu.karcags.mythscape.modules.application.db.UsersTable
import eu.karcags.mythscape.utils.current
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.datetime

object CampaignMembersTable : IntIdTable("campaign_members") {
    val name = varchar("name", 80)
    val campaign = reference("campaign_id", CampaignsTable, onDelete = ReferenceOption.CASCADE)
    val user = reference("user_id", UsersTable, onDelete = ReferenceOption.SET_NULL).nullable()
    val creation = datetime("creation").default(current())
    val isDM = bool("is_dm").default(false)
}