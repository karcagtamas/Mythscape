package eu.karcags.mythscape.plugins

import eu.karcags.mythscape.utils.ModuleRegistry
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.configureRouting(registry: ModuleRegistry) {
    routing {
        swaggerUI(path = "openapi")
        openAPI(path = "openapi")

        route("/api") {
            registry.openRoutesForAll(this)

            authenticate("auth-jwt") {
                registry.protectedRoutesForAll(this@route)
            }
        }
    }
}
