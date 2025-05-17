package eu.karcags.mythscape.controllers

import eu.karcags.mythscape.dtos.campaigns.*
import eu.karcags.mythscape.dtos.notes.treeDTO
import eu.karcags.mythscape.repositories.CampaignRepository
import eu.karcags.mythscape.utils.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.campaignController(repository: CampaignRepository) {
    route("/campaigns") {
        get("/user/{userId}") {
            val userId = call.parameters["userId"]?.toIntOrNull().requireNonNull()

            call.respond(repository.byUserId(userId) { it.campaignDTO() }.wrap())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            call.respond(repository.get(id) { it.campaignDTO() }.required().wrap())
        }

        get("/{id}/tags") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            call.respond(repository.getTags(id) { it.campaignTagDTO() }.wrap())
        }

        get("/{id}/members") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            call.respond(repository.getMembers(id) { it.campaignMemberDTO() }.wrap())
        }

        get("/{id}/notes") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            call.respond(repository.get(id) { it.treeDTO() }.required().wrap())
        }

        post {
            val dto = call.receive<CampaignEditDTO>()
            val principal = call.principal<UserPrincipal>().required()

            val id = repository.create {
                name = dto.name
                title = dto.title
                description = dto.description
                creation = current()
                lastUpdate = current()
                creator = principal.user
            }

            call.respond(id.wrap(HttpStatusCode.Created))
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()
            val dto = call.receive<CampaignEditDTO>()

            repository.update(id) {
                name = dto.name
                title = dto.title
                description = dto.description
                lastUpdate = current()
            }

            call.respond(success())
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            repository.delete(id)

            call.respond(success())
        }

        post("/{id}/tags") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()
            val dto = call.receive<CampaignTagEditDTO>()

            val campaign = repository.get(id).required()

            val tagId = repository.createTag {
                caption = dto.caption
                color = dto.color
                creation = current()
                this.campaign = campaign
            }

            call.respond(tagId.wrap(HttpStatusCode.Created))
        }

        delete("/{id}/tags/{tagId}") {
            val tagId = call.parameters["tagId"]?.toIntOrNull().requireNonNull()

            repository.deleteTag(tagId)

            call.respond(success())
        }
    }
}