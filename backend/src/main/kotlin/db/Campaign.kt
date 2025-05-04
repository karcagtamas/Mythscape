package eu.karcags.mythscape.db

import eu.karcags.mythscape.utils.current
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Campaigns : IntIdTable("campaigns") {

    val name = varchar("name", 40)
    val title = varchar("title", 120)
    val imageId = integer("image_id").nullable()
    val description = text("description").nullable()
    val creator = reference("creator_id", Users, onDelete = ReferenceOption.CASCADE)
    val creation = datetime("creation").default(current())
    val lastUpdate = datetime("last_update").default(current())
}

class Campaign(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Campaign>(Campaigns)

    var name by Campaigns.name
    var title by Campaigns.title
    var imageId by Campaigns.imageId
    var description by Campaigns.description
    var creator by User referencedOn Campaigns.creator
    var creation by Campaigns.creation
    var lastUpdate by Campaigns.lastUpdate
    val members by CampaignMember referrersOn CampaignMembers.campaign
    val tags by CampaignTag referrersOn CampaignTags.campaign
}