package eu.karcags.mythscape.modules.campaign.routes

import eu.karcags.mythscape.dtos.campaigns.*
import eu.karcags.mythscape.dtos.notes.treeDTO
import eu.karcags.mythscape.modules.campaign.dao.CampaignEntity
import eu.karcags.mythscape.modules.campaign.dao.CampaignMemberEntity
import eu.karcags.mythscape.modules.campaign.dao.CampaignTagEntity
import eu.karcags.mythscape.modules.campaign.db.CampaignMembersTable
import eu.karcags.mythscape.modules.campaign.db.CampaignTagsTable
import eu.karcags.mythscape.modules.campaign.db.CampaignsTable
import eu.karcags.mythscape.utils.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.core.eq

fun Route.campaignRoutes() {
    route("/campaigns") {
        get("/user/{userId}") {
            val userId = call.parameters["userId"]?.toIntOrNull().requireNonNull()

            val campaigns = dbQuery {
                CampaignEntity.find {
                    CampaignsTable.creator eq userId
                }.toList().map { it.campaignDTO() }
            }

            call.wrapped(campaigns)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            val campaign = dbQuery { CampaignEntity.findById(id).required().campaignDTO() }

            call.wrapped(campaign)
        }

        get("/{id}/tags") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            val tags = dbQuery {
                CampaignTagEntity.find {
                    CampaignTagsTable.campaign eq id
                }.toList().map { it.campaignTagDTO() }
            }

            call.wrapped(tags)
        }

        get("/{id}/members") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            val members = dbQuery {
                CampaignMemberEntity.find {
                    CampaignMembersTable.campaign eq id
                }.toList().map { it.campaignMemberDTO() }
            }

            call.wrapped(members)
        }

        get("/{id}/notes") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            val notes = dbQuery {
                CampaignEntity.findById(id).required().treeDTO()
            }

            call.wrapped(notes)
        }

        post {
            val dto = call.receive<CampaignEditDTO>()
            val principal = call.principal<UserPrincipal>().required()

            val campaign = dbQuery {
                CampaignEntity.new {
                    name = dto.name
                    title = dto.title
                    description = dto.description
                    creation = current()
                    lastUpdate = current()
                    creator = principal.user
                }
            }

            call.wrapped(campaign.id.value, HttpStatusCode.Created)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()
            val dto = call.receive<CampaignEditDTO>()

            dbQuery {
                CampaignEntity.findByIdAndUpdate(id) {
                    it.name = dto.name
                    it.title = dto.title
                    it.description = dto.description
                    it.lastUpdate = current()
                }
            }

            call.success()
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            dbQuery {
                CampaignEntity.findById(id).required().delete()
            }

            call.success()
        }

        post("/{id}/tags") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()
            val dto = call.receive<CampaignTagEditDTO>()

            val tag = dbQuery {
                val campaign = CampaignEntity.findById(id).required()

                CampaignTagEntity.new {
                    caption = dto.caption
                    color = dto.color
                    creation = current()
                    this.campaign = campaign
                }
            }

            call.wrapped(tag.id.value, HttpStatusCode.Created)
        }

        delete("/{id}/tags/{tagId}") {
            val tagId = call.parameters["tagId"]?.toIntOrNull().requireNonNull()

            dbQuery {
                CampaignTagEntity.findById(tagId).required().delete()
            }

            call.success()
        }
    }
}