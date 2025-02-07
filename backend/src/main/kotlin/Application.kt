package eu.karcags.mythscape

import eu.karcags.mythscape.repositories.UserRepository
import eu.karcags.mythscape.repositories.impl.UserRepositoryImpl
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val koinModule = module {
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080) {

    }.start(wait = true)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(koinModule)
    }

    configureRouting()
    configureDatabases()

    install(ContentNegotiation) {
        json()
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
}
