package eu.karcags.mythscape.modules.campaign.routes

import eu.karcags.mythscape.dtos.campaigns.CampaignRequestDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignTagEditDTO
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
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.neq

fun Route.campaignRoutes() {
    route("/campaigns") {
        get("/user/{userId}") {
            val userId = call.parameters["userId"]?.toIntOrNull().requireNonNull()

            val campaigns = dbQuery {
                CampaignEntity.find {
                    (CampaignsTable.creator eq userId) and (CampaignsTable.archived neq Op.TRUE)
                }.toList().map { it.dto() }
            }

            call.wrapped(campaigns)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            val campaign = dbQuery { CampaignEntity.findById(id).required().dto() }

            call.wrapped(campaign)
        }

        get("/{id}/tags") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            val tags = dbQuery {
                CampaignTagEntity.find {
                    CampaignTagsTable.campaign eq id
                }.toList().map { it.dto() }
            }

            call.wrapped(tags)
        }

        get("/{id}/members") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            val members = dbQuery {
                CampaignMemberEntity.find {
                    CampaignMembersTable.campaign eq id
                }.toList().map { it.dto() }
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
            val dto = call.receive<CampaignRequestDTO>()
            val principal = call.principal<UserPrincipal>().required()

            val data = dbQuery {
                val entity = CampaignEntity.new {
                    name = dto.name
                    title = dto.title
                    description = dto.description
                    creation = current()
                    lastUpdate = current()
                    creator = principal.user
                }

                CampaignMemberEntity.new {
                    name = principal.user.name
                    campaign = entity
                    user = principal.user
                    creation = current()
                    isDM = true
                }

                entity.dto()
            }

            call.wrapped(data, HttpStatusCode.Created)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()
            val dto = call.receive<CampaignRequestDTO>()

            val data = dbQuery {
                CampaignEntity.findByIdAndUpdate(id) {
                    it.name = dto.name
                    it.title = dto.title
                    it.description = dto.description
                    it.lastUpdate = current()
                }.required().dto()
            }

            call.wrapped(data)
        }

        put("/{id}/archive") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            dbQuery {
                CampaignEntity.findByIdAndUpdate(id) {
                    it.archived = true
                }.required()
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