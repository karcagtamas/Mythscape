package eu.karcags.mythscape.db

import eu.karcags.mythscape.utils.current
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object CampaignTags : IntIdTable("campaign_tags") {
    val campaign = reference("campaign_id", Campaigns, onDelete = ReferenceOption.CASCADE)
    val caption = varchar("caption", 40)
    val color = varchar("color", 7)
    val creation = datetime("creation").default(current())
}

class CampaignTag(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CampaignTag>(CampaignTags)

    var campaign by Campaign referencedOn CampaignTags.campaign
    var caption by CampaignTags.caption
    var color by CampaignTags.color
    var creation by CampaignTags.creation
}