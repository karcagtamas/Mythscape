package eu.karcags.mythscape.plugins

import eu.karcags.mythscape.modules.application.ApplicationModule
import eu.karcags.mythscape.modules.campaign.CampaignModule
import eu.karcags.mythscape.modules.note.NoteModule
import eu.karcags.mythscape.utils.ModuleRegistry
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val appModule = module {
    single {
        ModuleRegistry(
            listOf(
                ApplicationModule(),
                CampaignModule(),
                NoteModule(),
            )
        )
    }
}

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(
            module { single { this@configureKoin } },
            appModule,
        )
    }
}