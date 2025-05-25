package eu.karcags.mythscape.controllers

import eu.karcags.mythscape.dtos.sessions.SessionEditDTO
import eu.karcags.mythscape.dtos.sessions.sessionDTO
import eu.karcags.mythscape.repositories.CampaignRepository
import eu.karcags.mythscape.repositories.SessionRepository
import eu.karcags.mythscape.utils.requireNonNull
import eu.karcags.mythscape.utils.required
import eu.karcags.mythscape.utils.success
import eu.karcags.mythscape.utils.wrap
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.sessionController(repository: SessionRepository, campaignRepository: CampaignRepository) {
    route("/sessions") {
        get {
            val campaignId = call.queryParameters["campaignId"]?.toIntOrNull()
            val showAll = call.queryParameters["showAll"]?.toBoolean() ?: false

            call.respond(repository.query(campaignId, showAll) { it.sessionDTO() }.wrap())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            call.respond(repository.get(id) { it.sessionDTO() }.required().wrap())
        }

        post {
            val dto = call.receive<SessionEditDTO>()

            val campaign = campaignRepository.get(dto.campaignId).required()

            val id = repository.create {
                date = dto.date
                startTime = dto.startTime
                endTime = dto.endTime
                this.campaign = campaign
            }

            call.respond(id.wrap(HttpStatusCode.Created))
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()
            val dto = call.receive<SessionEditDTO>()

            repository.update(id) {
                date = dto.date
                startTime = dto.startTime
                endTime = dto.endTime
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