package eu.karcags.mythscape.modules.campaign.dao

import eu.karcags.mythscape.modules.campaign.db.CampaignMembersTable
import eu.karcags.mythscape.modules.application.dao.UserEntity
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class CampaignMemberEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CampaignMemberEntity>(CampaignMembersTable)

    var name by CampaignMembersTable.name
    var campaign by CampaignEntity referencedOn CampaignMembersTable.campaign
    var user by UserEntity optionalReferencedOn CampaignMembersTable.user
    var creation by CampaignMembersTable.creation
    var isDM by CampaignMembersTable.isDM
}