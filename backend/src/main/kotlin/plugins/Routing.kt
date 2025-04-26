package eu.karcags.mythscape.plugins

import eu.karcags.mythscape.controllers.authenticationController
import eu.karcags.mythscape.controllers.campaignController
import eu.karcags.mythscape.controllers.fileController
import eu.karcags.mythscape.controllers.userController
import eu.karcags.mythscape.repositories.CampaignRepository
import eu.karcags.mythscape.repositories.FileRepository
import eu.karcags.mythscape.repositories.RefreshTokenRepository
import eu.karcags.mythscape.repositories.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val refreshTokenRepository by inject<RefreshTokenRepository>()
    val userRepository by inject<UserRepository>()
    val campaignRepository by inject<CampaignRepository>()
    val fileRepository by inject<FileRepository>()

    routing {
        swaggerUI(path = "openapi")
        openAPI(path = "openapi")

        route("/api") {
            authenticationController(userRepository, refreshTokenRepository)

            authenticate("auth-jwt") {
                userController(userRepository)
                campaignController(campaignRepository)
                fileController(fileRepository)
            }
        }
    }
}
