package eu.karcags.mythscape.modules.notedsl

import eu.karcags.mythscape.modules.notedsl.routes.noteDslRoutes
import eu.karcags.mythscape.notedsl.NoteDslEvaluator
import eu.karcags.mythscape.utils.AppModule
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.core.module.Module
import org.koin.dsl.module

class NoteDslModule : AppModule {

    override fun Application.register() {}

    override fun Route.openRoutes() {}

    override fun Route.protectedRoutes() {
        noteDslRoutes()
    }

    override fun module(): Module = module {
        single { NoteDslEvaluator() }
    }

}