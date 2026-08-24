package eu.karcags.mythscape.modules.application

import eu.karcags.mythscape.modules.application.routes.authenticationRoutes
import eu.karcags.mythscape.modules.application.routes.fileRoutes
import eu.karcags.mythscape.modules.application.routes.userRoutes
import eu.karcags.mythscape.modules.application.services.UserService
import eu.karcags.mythscape.modules.application.services.UserServiceImpl
import eu.karcags.mythscape.modules.application.services.RefreshTokenService
import eu.karcags.mythscape.modules.application.services.RefreshTokenServiceImpl
import eu.karcags.mythscape.utils.AppModule
import io.ktor.server.application.Application
import io.ktor.server.routing.Route
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ktor.ext.inject

class ApplicationModule : AppModule {

    override fun Application.register() {}

    override fun Route.openRoutes() {
        val refreshTokenService by inject<RefreshTokenService>()
        val userService by inject<UserService>()

        authenticationRoutes(refreshTokenService, userService)
    }

    override fun Route.protectedRoutes() {
        userRoutes()
        fileRoutes()
    }

    override fun module(): Module = module {
        single<RefreshTokenService> { RefreshTokenServiceImpl() }
        single<UserService> { UserServiceImpl() }
    }
}