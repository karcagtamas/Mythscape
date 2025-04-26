package eu.karcags.mythscape.controllers

import eu.karcags.mythscape.dtos.dto
import eu.karcags.mythscape.repositories.UserRepository
import eu.karcags.mythscape.utils.UserPrincipal
import eu.karcags.mythscape.utils.requireNonNull
import eu.karcags.mythscape.utils.required
import eu.karcags.mythscape.utils.wrap
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

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