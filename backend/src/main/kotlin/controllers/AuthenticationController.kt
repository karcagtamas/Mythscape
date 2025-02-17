package eu.karcags.mythscape.controllers

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import eu.karcags.mythscape.ConfigKey
import eu.karcags.mythscape.dtos.LoginDTO
import eu.karcags.mythscape.repositories.UserRepository
import eu.karcags.mythscape.utils.getStringProperty
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.authenticationController(userRepository: UserRepository) {
    route("/auth") {
        post("/login") {
            val data = call.receive<LoginDTO>()

            val user = userRepository.findByUsername(data.username)

            // TODO: Verify password

            if (user != null) {
                val token = JWT.create()
                    .withAudience(environment.config.getStringProperty(ConfigKey.JWT_AUDIENCE))
                    .withIssuer(environment.config.getStringProperty(ConfigKey.JWT_ISSUER))
                    .withClaim("userId", user.id.value)
                    .withClaim("username", data.username)
                    .withExpiresAt(Date(System.currentTimeMillis() + 60 * 1000))
                    .sign(Algorithm.HMAC256(environment.config.getStringProperty(ConfigKey.JWT_SECRET)))

                call.respond(hashMapOf("token" to token))
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}