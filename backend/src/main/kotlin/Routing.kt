package eu.karcags

import eu.karcags.models.User
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/users") {
            get {

            }

            post {
                val user = call.receive<User>()

            }
        }
    }
}
