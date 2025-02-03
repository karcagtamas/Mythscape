package eu.karcags

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            json()
        }

        install(StatusPages) {
            exception<Throwable> { call, cause ->
                call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
            }
        }
    }.start(wait = true)
}

fun Application.module() {
    configureRouting()
}
