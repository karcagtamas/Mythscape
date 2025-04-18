package eu.karcags.mythscape.controllers

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import eu.karcags.mythscape.ConfigKey
import eu.karcags.mythscape.dtos.auth.*
import eu.karcags.mythscape.dtos.dto
import eu.karcags.mythscape.repositories.RefreshTokenRepository
import eu.karcags.mythscape.repositories.UserRepository
import eu.karcags.mythscape.utils.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import org.mindrot.jbcrypt.BCrypt
import java.util.*

fun Route.authenticationController(userRepository: UserRepository, refreshTokenRepository: RefreshTokenRepository) {
    route("/auth") {
        post("/login") {
            val data = call.receive<LoginDTO>()

            val user = userRepository.findByUsername(data.username) ?: throw ServerException.Unauthorized("User not found with username: ${data.username}")

            if (!BCrypt.checkpw(data.password, user.password)) {
                throw ServerException.Unauthorized("Incorrect password was provided.")
            }

            val token = createToken(environment.config, user.id.value, data.username)

            val clientId = UUID.randomUUID().toString()
            val refreshToken = generateRefreshToken(refreshTokenRepository, clientId, user.id.value)

            call.respond(TokenDTO(token, user.dto(), refreshToken, clientId).wrap())
        }

        post("/register") {
            val data = call.receive<RegisterDTO>()

            if (userRepository.existsByUsernameOrEmail(data.username, data.email)) {
                throw ServerException("Username or email already exists.", HttpStatusCode.BadRequest)
            }

            val hashedPassword = BCrypt.hashpw(data.password, BCrypt.gensalt())

            val id = userRepository.create {
                username = data.username
                email = data.email
                password = hashedPassword
                name = data.fullname
                register = current()
            }

            call.respond(id.wrap(HttpStatusCode.Created))
        }

        post("/refresh") {
            val data = call.receive<RefreshDTO>()

            val refreshToken = refreshTokenRepository.find(data) ?: throw ServerException.Forbidden("Refresh token is not valid.")
            val user = userRepository.get(data.userId) ?: throw ServerException.NotFound()

            val token = createToken(environment.config, data.userId, user.username)
            revokeRefreshToken(refreshTokenRepository, refreshToken.id.value)
            val newRefreshToken = generateRefreshToken(refreshTokenRepository, data.clientId, data.userId)

            call.respond(TokenDTO(token, user.dto(), newRefreshToken, data.clientId).wrap())
        }

        post("/logout") {
            val data = call.receive<LogoutDTO>()
            refreshTokenRepository.revokeAll(data.userId, data.clientId)
            call.respond(success())
        }
    }
}

fun createToken(config: ApplicationConfig, userId: Int, username: String): String {
    return JWT.create()
        .withAudience(config.getStringProperty(ConfigKey.JWT_AUDIENCE))
        .withIssuer(config.getStringProperty(ConfigKey.JWT_ISSUER))
        .withClaim("userId", userId)
        .withClaim("username", username)
        .withExpiresAt(Date(System.currentTimeMillis() + config.getIntProperty(ConfigKey.JWT_EXPIRATION) * 1000))
        .sign(Algorithm.HMAC256(config.getStringProperty(ConfigKey.JWT_SECRET)))
}

suspend fun revokeRefreshToken(refreshTokenRepository: RefreshTokenRepository, refreshTokenId: Int) {
    refreshTokenRepository.update(refreshTokenId) {
        revoked = current()
    }
}

suspend fun generateRefreshToken(refreshTokenRepository: RefreshTokenRepository, clientId: String, userId: Int): String {
    val token = UUID.randomUUID().toString()

    refreshTokenRepository.create {
        this.userId = userId
        this.clientId = clientId
        this.token = token
        expiration = current().toInstant(TimeZone.UTC).plus(1, DateTimeUnit.DAY, TimeZone.UTC).toLocalDateTime(TimeZone.UTC)
    }

    return token
}