package eu.karcags.mythscape.controllers

import eu.karcags.mythscape.dtos.campaigns.noteDataDTO
import eu.karcags.mythscape.repositories.NoteRepository
import eu.karcags.mythscape.utils.requireNonNull
import eu.karcags.mythscape.utils.required
import eu.karcags.mythscape.utils.wrap
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.noteController(repository: NoteRepository) {
    route("/notes") {
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            call.respond(repository.get(id) { it.noteDataDTO() }.required().wrap())
        }
    }
}