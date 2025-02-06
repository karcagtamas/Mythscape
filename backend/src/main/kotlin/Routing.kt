package eu.karcags.mythscape

import eu.karcags.mythscape.controllers.userController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        userController()
    }
}
