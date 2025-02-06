package eu.karcags.mythscape

import eu.karcags.mythscape.controllers.userController
import eu.karcags.mythscape.repositories.UserRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository by inject<UserRepository>()

    routing {
        route("/api") {
            userController(userRepository)
        }
    }
}
