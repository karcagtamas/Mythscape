package eu.karcags.mythscape.utils

import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import org.koin.core.module.Module

interface AppModule {

    fun Application.register()

    fun Route.openRoutes()

    fun Route.protectedRoutes()

    fun module(): Module
}

class ModuleRegistry(private val modules: List<AppModule>) {

    fun registerAll(application: Application) {
        modules.forEach { module ->
            application.log.info("Registering ${module::class.simpleName}")
            module.run { application.register() }
        }
    }

    fun openRoutesForAll(route: Route) {
        modules.forEach { module ->
            route.application.log.info("Applying ${module::class.simpleName} open routes")
            module.run { route.openRoutes() }
        }
    }

    fun protectedRoutesForAll(route: Route) {
        modules.forEach { module ->
            route.application.log.info("Applying ${module::class.simpleName} protected routes")
            module.run { route.protectedRoutes() }
        }
    }

    fun modules(): List<Module> {
        return modules.map { it.module() }
    }
}