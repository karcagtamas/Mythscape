package eu.karcags.mythscape.controllers

import eu.karcags.mythscape.dtos.campaigns.CampaignEditDTO
import eu.karcags.mythscape.dtos.campaigns.dto
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

            call.respond(repository.byUserId(userId) { it.dto() }.wrap())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            call.respond(repository.get(id) { it.dto() }.required().wrap())
        }

        get("/{id}/tags") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            call.respond(repository.getTags(id) { it.dto() }.wrap())
        }

        get("/{id}/members") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            call.respond(repository.getMembers(id) { it.dto() }.wrap())
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
    }
}