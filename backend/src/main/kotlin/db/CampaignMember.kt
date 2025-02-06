package eu.karcags.mythscape.db

import eu.karcags.mythscape.utils.current
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object CampaignMembers : IntIdTable("campaign_members") {
    val name = varchar("name", 80)
    val campaign = reference("campaign_id", Campaigns)
    val user = reference("user_id", Users).nullable()
    val creation = datetime("creation").default(current())
    val isDM = bool("is_dm").default(false)
}

class CampaignMember(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CampaignMember>(CampaignMembers)

    var name by CampaignMembers.name
    var campaign by Campaign referencedOn CampaignMembers.campaign
    var user by User optionalReferencedOn CampaignMembers.user
    var creation by CampaignMembers.creation
    var isDM by CampaignMembers.isDM
}