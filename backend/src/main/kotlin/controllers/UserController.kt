package eu.karcags.mythscape.controllers

import eu.karcags.mythscape.dtos.campaigns.dto
import eu.karcags.mythscape.repositories.UserRepository
import eu.karcags.mythscape.utils.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.userController(repository: UserRepository) {
    route("/users") {
        get {
            call.respond(repository.all().dto().wrap())
        }

        get("/{id}") {
            val userId = call.parameters["id"]?.toIntOrNull().requireNonNull()

            call.respond(repository.get(userId).required().dto().wrap())
        }

        get("/current") {
            val principal = call.principal<UserPrincipal>()

            call.respond(principal.required().user.dto().wrap())
        }
    }
}