package eu.karcags.mythscape.controllers

import eu.karcags.mythscape.dtos.dto
import eu.karcags.mythscape.repositories.UserRepository
import eu.karcags.mythscape.utils.UserPrincipal
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userController(repository: UserRepository) {
    route("/users") {
        get {
            call.respond(repository.all().dto())
        }

        get("/{id}") {
            val userId = call.parameters["id"]?.toIntOrNull()

            if (userId != null) {
                call.respondNullable(repository.get(userId)?.dto())
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("/current") {
            val principal = call.principal<UserPrincipal>()

            call.respond(principal!!.user.dto())
        }
    }
}