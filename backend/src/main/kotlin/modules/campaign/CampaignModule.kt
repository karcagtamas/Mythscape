package eu.karcags.mythscape.modules.campaign

import eu.karcags.mythscape.modules.campaign.routes.campaignRoutes
import eu.karcags.mythscape.modules.campaign.routes.sessionRoutes
import eu.karcags.mythscape.utils.AppModule
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.core.module.Module
import org.koin.dsl.module

class CampaignModule : AppModule {
    override fun Application.register() {}

    override fun Route.openRoutes() {}

    override fun Route.protectedRoutes() {
        campaignRoutes()
        sessionRoutes()
    }

    override fun module(): Module = module {}
}