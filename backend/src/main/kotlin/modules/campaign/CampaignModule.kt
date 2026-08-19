package eu.karcags.mythscape.modules.campaign

import eu.karcags.mythscape.modules.campaign.routes.campaignRoutes
import eu.karcags.mythscape.modules.campaign.routes.sessionRoutes
import eu.karcags.mythscape.repositories.CampaignRepository
import eu.karcags.mythscape.repositories.SessionRepository
import eu.karcags.mythscape.repositories.impl.CampaignRepositoryImpl
import eu.karcags.mythscape.repositories.impl.SessionRepositoryImpl
import eu.karcags.mythscape.utils.AppModule
import io.ktor.server.application.Application
import io.ktor.server.routing.Route
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ktor.ext.inject

class CampaignModule : AppModule {
    override fun Application.register() {}

    override fun Route.openRoutes() {}

    override fun Route.protectedRoutes() {
        val campaignRepository by inject<CampaignRepository>()
        val sessionRepository by inject<SessionRepository>()

        campaignRoutes(campaignRepository)
        sessionRoutes(sessionRepository, campaignRepository)
    }

    override fun module(): Module = module {
        single<CampaignRepository> { CampaignRepositoryImpl() }
        single<SessionRepository> { SessionRepositoryImpl() }
    }
}