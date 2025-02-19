package eu.karcags.mythscape.plugins

import eu.karcags.mythscape.utils.Failure
import eu.karcags.mythscape.utils.ServerException
import eu.karcags.mythscape.utils.errorData
import eu.karcags.mythscape.utils.failure
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureErrorHandling() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is ServerException -> {
                    @Suppress("USELESS_CAST")
                    call.respond((cause as ServerException).failure())
                }

                is RequestValidationException -> {
                    call.respond(Failure(HttpStatusCode.BadRequest.value, cause.errorData()))
                }

                else -> {
                    call.respond(cause.failure())
                }
            }
        }
    }
}