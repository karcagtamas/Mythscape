package eu.karcags.mythscape.modules.application.routes

import eu.karcags.mythscape.dtos.dto
import eu.karcags.mythscape.modules.application.dao.UserEntity
import eu.karcags.mythscape.utils.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.userRoutes() {
    route("/users") {
        get {
            val users = dbQuery {
                UserEntity.all().map {
                    it.dto()
                }
            }

            call.wrapped(users)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()
            val user = dbQuery { UserEntity.findById(id).required().dto() }

            call.wrapped(user)
        }

        get("/current") {
            val principal = call.principal<UserPrincipal>()

            call.wrapped(principal.required().user.dto())
        }
    }
}