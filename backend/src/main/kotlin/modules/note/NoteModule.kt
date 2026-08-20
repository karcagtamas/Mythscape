package eu.karcags.mythscape.modules.note

import eu.karcags.mythscape.modules.note.routes.noteRoutes
import eu.karcags.mythscape.utils.AppModule
import io.ktor.server.application.Application
import io.ktor.server.routing.Route
import org.koin.core.module.Module
import org.koin.dsl.module

class NoteModule : AppModule {
    override fun Application.register() {}

    override fun Route.openRoutes() {}

    override fun Route.protectedRoutes() {
        noteRoutes()
    }

    override fun module(): Module = module {}
}