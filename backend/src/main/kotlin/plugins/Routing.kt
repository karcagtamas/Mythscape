package eu.karcags.mythscape.plugins

import eu.karcags.mythscape.modules.campaign.routes.campaignRoutes
import eu.karcags.mythscape.modules.note.routes.noteRoutes
import eu.karcags.mythscape.modules.campaign.routes.sessionRoutes
import eu.karcags.mythscape.repositories.CampaignRepository
import eu.karcags.mythscape.repositories.NoteRepository
import eu.karcags.mythscape.repositories.SessionRepository
import eu.karcags.mythscape.utils.ModuleRegistry
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting(registry: ModuleRegistry) {
    val campaignRepository by inject<CampaignRepository>()
    val noteRepository by inject<NoteRepository>()
    val sessionRepository by inject<SessionRepository>()

    routing {
        swaggerUI(path = "openapi")
        openAPI(path = "openapi")

        route("/api") {
            registry.openRoutesForAll(this)

            authenticate("auth-jwt") {
                campaignRoutes(campaignRepository)
                noteRoutes(noteRepository)
                sessionRoutes(sessionRepository, campaignRepository)

                registry.protectedRoutesForAll(this@route)
            }
        }
    }
}
