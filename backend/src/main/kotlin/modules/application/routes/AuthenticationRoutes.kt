package eu.karcags.mythscape.modules.application.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import eu.karcags.mythscape.ConfigKey
import eu.karcags.mythscape.dtos.auth.*
import eu.karcags.mythscape.dtos.dto
import eu.karcags.mythscape.modules.application.dao.RefreshTokenEntity
import eu.karcags.mythscape.modules.application.dao.UserEntity
import eu.karcags.mythscape.modules.application.services.UserService
import eu.karcags.mythscape.modules.application.services.RefreshTokenService
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

fun Route.authenticationRoutes(
    refreshTokenService: RefreshTokenService,
    userService: UserService,
) {
    route("/auth") {
        post("/login") {
            val data = call.receive<LoginDTO>()

            val user = dbQuery {
                userService.getByName(data.username)
            } ?: throw ServerException.Unauthorized("User not found with username: ${data.username}")

            if (!BCrypt.checkpw(data.password, user.password)) {
                throw ServerException.Unauthorized("Incorrect password was provided.")
            }

            val token = createToken(environment.config, user.id.value, data.username)

            val clientId = UUID.randomUUID().toString()
            val refreshToken = generateRefreshToken(clientId, user.id.value)

            call.wrapped(TokenDTO(token, user.dto(), refreshToken, clientId))
        }

        post("/register") {
            val data = call.receive<RegisterDTO>()

            val existsByUsernameOrEmail = dbQuery {
                userService.existsByUsernameOrEmail(data.username, data.email)
            }

            if (existsByUsernameOrEmail) {
                throw ServerException("Username or email already exists.", HttpStatusCode.BadRequest)
            }

            val hashedPassword = BCrypt.hashpw(data.password, BCrypt.gensalt())

            val user = dbQuery {
                UserEntity.new {
                    username = data.username
                    email = data.email
                    password = hashedPassword
                    name = data.fullname
                    register = current()
                }
            }

            call.wrapped(user.id.value, HttpStatusCode.Created)
        }

        post("/refresh") {
            val data = call.receive<RefreshDTO>()

            val refreshToken = dbQuery {
                refreshTokenService.find(data) ?: throw ServerException.Forbidden("Refresh token is not valid.")
            }

            val user = dbQuery {
                UserEntity.findById(data.userId)
            }.required()

            val token = createToken(environment.config, data.userId, user.username)
            revokeRefreshToken(refreshToken.id.value)
            val newRefreshToken = generateRefreshToken(data.clientId, data.userId)

            call.wrapped(TokenDTO(token, user.dto(), newRefreshToken, data.clientId))
        }

        post("/logout") {
            val data = call.receive<LogoutDTO>()
            dbQuery {
                refreshTokenService.revokeAll(data.userId, data.clientId)
            }
            call.success()
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

suspend fun revokeRefreshToken(refreshTokenId: Int) {
    dbQuery {
        RefreshTokenEntity.findByIdAndUpdate(refreshTokenId) {
            it.revoked = current()
        }
    }
}

suspend fun generateRefreshToken(
    clientId: String,
    userId: Int
): String {
    val token = UUID.randomUUID().toString()

    dbQuery {
        RefreshTokenEntity.new {
            this.userId = userId
            this.clientId = clientId
            this.token = token
            expiration =
                current().toInstant(TimeZone.UTC).plus(1, DateTimeUnit.DAY, TimeZone.UTC).toLocalDateTime(TimeZone.UTC)
        }
    }

    return token
}