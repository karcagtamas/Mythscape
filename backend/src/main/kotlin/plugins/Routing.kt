package eu.karcags.mythscape.plugins

import eu.karcags.mythscape.controllers.userController
import eu.karcags.mythscape.repositories.UserRepository
import io.ktor.server.application.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository by inject<UserRepository>()

    routing {
        swaggerUI(path = "openapi")
        openAPI(path = "openapi")

        route("/api") {
            userController(userRepository)
        }
    }
}
