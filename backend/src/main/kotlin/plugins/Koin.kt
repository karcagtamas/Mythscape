package eu.karcags.mythscape.plugins

import eu.karcags.mythscape.repositories.CampaignRepository
import eu.karcags.mythscape.repositories.FileRepository
import eu.karcags.mythscape.repositories.NoteRepository
import eu.karcags.mythscape.repositories.RefreshTokenRepository
import eu.karcags.mythscape.repositories.SessionRepository
import eu.karcags.mythscape.repositories.UserRepository
import eu.karcags.mythscape.repositories.impl.UserRepositoryImpl
import eu.karcags.mythscape.repositories.impl.RefreshTokenRepositoryImpl
import eu.karcags.mythscape.repositories.impl.CampaignRepositoryImpl
import eu.karcags.mythscape.repositories.impl.FileRepositoryImpl
import eu.karcags.mythscape.repositories.impl.NoteRepositoryImpl
import eu.karcags.mythscape.repositories.impl.SessionRepositoryImpl
import io.ktor.server.application.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val koinModule = module {
    singleOf(::RefreshTokenRepositoryImpl) { bind<RefreshTokenRepository>() }
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    singleOf(::CampaignRepositoryImpl) { bind<CampaignRepository>() }
    singleOf(::FileRepositoryImpl) { bind<FileRepository>() }
    singleOf(::NoteRepositoryImpl) { bind<NoteRepository>() }
    singleOf(::SessionRepositoryImpl) { bind<SessionRepository>() }
}

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(koinModule)
    }
}