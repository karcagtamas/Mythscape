package eu.karcags.mythscape.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import eu.karcags.mythscape.ConfigKey
import eu.karcags.mythscape.repositories.UserRepository
import eu.karcags.mythscape.utils.UserPrincipal
import eu.karcags.mythscape.utils.getStringProperty
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {
    val appRealm = environment.config.getStringProperty(ConfigKey.JWT_REALM)
    val secret = environment.config.getStringProperty(ConfigKey.JWT_SECRET)
    val audience = environment.config.getStringProperty(ConfigKey.JWT_AUDIENCE)
    val issuer = environment.config.getStringProperty(ConfigKey.JWT_ISSUER)

    val userRepository by inject<UserRepository>()

    install(Authentication) {
        jwt("auth-jwt") {
            realm = appRealm

            verifier(
                JWT.require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )

            validate { credential ->
                if (credential.payload.getClaim("userId").isMissing) {
                    null
                } else {
                    val user = userRepository.get(credential.payload.getClaim("userId").asInt())

                    if (user == null) {
                        null
                    } else {
                        UserPrincipal(user)
                    }
                }
            }

            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}