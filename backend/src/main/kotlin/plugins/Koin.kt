package eu.karcags.mythscape.plugins

import eu.karcags.mythscape.repositories.UserRepository
import eu.karcags.mythscape.repositories.impl.UserRepositoryImpl
import io.ktor.server.application.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val koinModule = module {
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
}

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(koinModule)
    }
}