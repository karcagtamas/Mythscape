package eu.karcags.mythscape.controllers

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import eu.karcags.mythscape.ConfigKey
import eu.karcags.mythscape.dtos.LoginDTO
import eu.karcags.mythscape.dtos.RegisterDTO
import eu.karcags.mythscape.repositories.UserRepository
import eu.karcags.mythscape.utils.current
import eu.karcags.mythscape.utils.getStringProperty
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.mindrot.jbcrypt.BCrypt
import java.util.*

fun Route.authenticationController(userRepository: UserRepository) {
    route("/auth") {
        post("/login") {
            val data = call.receive<LoginDTO>()

            val user = userRepository.findByUsername(data.username)

            if (user != null) {
                if (!BCrypt.checkpw(data.password, user.password)) {
                    call.respond(HttpStatusCode.Unauthorized)
                } else {
                    val token = JWT.create()
                        .withAudience(environment.config.getStringProperty(ConfigKey.JWT_AUDIENCE))
                        .withIssuer(environment.config.getStringProperty(ConfigKey.JWT_ISSUER))
                        .withClaim("userId", user.id.value)
                        .withClaim("username", data.username)
                        .withExpiresAt(Date(System.currentTimeMillis() + 60 * 1000))
                        .sign(Algorithm.HMAC256(environment.config.getStringProperty(ConfigKey.JWT_SECRET)))

                    call.respond(hashMapOf("token" to token))
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        post("/register") {
            val data = call.receive<RegisterDTO>()

            if (userRepository.existsByUsernameOrEmail(data.username, data.email)) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val hashedPassword = BCrypt.hashpw(data.password, BCrypt.gensalt())

            userRepository.create {
                username = data.username
                email = data.email
                password = hashedPassword
                name = data.fullname
                register = current()
            }

            call.respond(HttpStatusCode.Created)
        }
    }
}