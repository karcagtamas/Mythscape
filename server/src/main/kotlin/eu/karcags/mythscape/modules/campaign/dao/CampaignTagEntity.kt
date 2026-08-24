package eu.karcags.mythscape.modules.campaign.dao

import eu.karcags.mythscape.modules.campaign.db.CampaignTagsTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class CampaignTagEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CampaignTagEntity>(CampaignTagsTable)

    var campaign by CampaignEntity referencedOn CampaignTagsTable.campaign
    var caption by CampaignTagsTable.caption
    var color by CampaignTagsTable.color
    var creation by CampaignTagsTable.creation
}