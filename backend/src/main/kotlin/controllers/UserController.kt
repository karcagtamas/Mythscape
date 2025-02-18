package eu.karcags.mythscape.controllers

import eu.karcags.mythscape.dtos.dto
import eu.karcags.mythscape.repositories.UserRepository
import eu.karcags.mythscape.utils.UserPrincipal
import eu.karcags.mythscape.utils.failure
import eu.karcags.mythscape.utils.wrap
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userController(repository: UserRepository) {
    route("/users") {
        get {
            call.respond(repository.all().dto().wrap())
        }

        get("/{id}") {
            val userId = call.parameters["id"]?.toIntOrNull()

            if (userId != null) {
                call.respond(repository.get(userId)?.dto().wrap())
            } else {
                call.respond(failure())
            }
        }

        get("/current") {
            val principal = call.principal<UserPrincipal>()

            call.respond(principal!!.user.dto().wrap())
        }
    }
}