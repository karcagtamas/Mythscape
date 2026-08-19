package eu.karcags.mythscape.modules.application

import eu.karcags.mythscape.modules.application.routes.authenticationRoutes
import eu.karcags.mythscape.modules.application.routes.fileRoutes
import eu.karcags.mythscape.modules.application.routes.userRoutes
import eu.karcags.mythscape.repositories.FileRepository
import eu.karcags.mythscape.repositories.RefreshTokenRepository
import eu.karcags.mythscape.repositories.UserRepository
import eu.karcags.mythscape.repositories.impl.FileRepositoryImpl
import eu.karcags.mythscape.repositories.impl.RefreshTokenRepositoryImpl
import eu.karcags.mythscape.repositories.impl.UserRepositoryImpl
import eu.karcags.mythscape.utils.AppModule
import io.ktor.server.application.Application
import io.ktor.server.routing.Route
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ktor.ext.inject

class ApplicationModule : AppModule {

    override fun Application.register() {}

    override fun Route.openRoutes() {
        val userRepository by inject<UserRepository>()
        val refreshTokenRepository by inject<RefreshTokenRepository>()

        authenticationRoutes(userRepository, refreshTokenRepository)
    }

    override fun Route.protectedRoutes() {
        val userRepository by inject<UserRepository>()
        val fileRepository by inject<FileRepository>()

        userRoutes(userRepository)
        fileRoutes(fileRepository)
    }

    override fun module(): Module = module {
        single<UserRepository> { UserRepositoryImpl() }
        single<RefreshTokenRepository> { RefreshTokenRepositoryImpl() }
        single<FileRepository> { FileRepositoryImpl() }
    }
}