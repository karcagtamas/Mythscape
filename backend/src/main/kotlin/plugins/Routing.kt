package eu.karcags.mythscape.plugins

import eu.karcags.mythscape.controllers.authenticationController
import eu.karcags.mythscape.controllers.campaignController
import eu.karcags.mythscape.controllers.userController
import eu.karcags.mythscape.repositories.CampaignRepository
import eu.karcags.mythscape.repositories.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository by inject<UserRepository>()
    val campaignRepository by inject<CampaignRepository>()

    routing {
        swaggerUI(path = "openapi")
        openAPI(path = "openapi")

        authenticationController(userRepository)

        authenticate("auth-jwt") {
            route("/api") {
                userController(userRepository)
                campaignController(campaignRepository)
            }
        }
    }
}
