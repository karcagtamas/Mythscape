package eu.karcags.mythscape.modules.note

import eu.karcags.mythscape.modules.note.routes.noteRoutes
import eu.karcags.mythscape.repositories.NoteRepository
import eu.karcags.mythscape.repositories.impl.NoteRepositoryImpl
import eu.karcags.mythscape.utils.AppModule
import io.ktor.server.application.Application
import io.ktor.server.routing.Route
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ktor.ext.inject

class NoteModule : AppModule {
    override fun Application.register() {}

    override fun Route.openRoutes() {}

    override fun Route.protectedRoutes() {
        val noteRepository by inject<NoteRepository>()

        noteRoutes(noteRepository)
    }

    override fun module(): Module = module {
        single<NoteRepository> { NoteRepositoryImpl() }
    }
}