package eu.karcags.mythscape.controllers

import eu.karcags.mythscape.dtos.sessions.sessionDTO
import eu.karcags.mythscape.repositories.SessionRepository
import eu.karcags.mythscape.utils.requireNonNull
import eu.karcags.mythscape.utils.required
import eu.karcags.mythscape.utils.wrap
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.sessionController(repository: SessionRepository) {
    route("/sessions") {
        get {
            val campaignId = call.queryParameters["campaignId"]?.toIntOrNull().requireNonNull()

            call.respond(repository.byCampaign(campaignId) { it.sessionDTO() }.wrap())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            call.respond(repository.get(id) { it.sessionDTO() }.required().wrap())
        }
    }
}