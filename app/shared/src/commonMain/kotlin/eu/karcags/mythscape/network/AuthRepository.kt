package eu.karcags.mythscape.network

import eu.karcags.mythscape.core.ServerResponse
import eu.karcags.mythscape.core.dtos.auth.LoginDTO
import eu.karcags.mythscape.core.dtos.auth.LogoutDTO
import eu.karcags.mythscape.core.dtos.auth.RefreshDTO
import eu.karcags.mythscape.core.dtos.auth.RegisterDTO
import eu.karcags.mythscape.core.dtos.auth.TokenDTO
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json

class AuthRepository(private val client: HttpClient) {

    suspend fun login(dto: LoginDTO): ServerResponse<TokenDTO> {
        val response = client.post("/api/auth/login") {
            setBody(dto)
        }

        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun register(dto: RegisterDTO): ServerResponse<Int> {
        val response = client.post("/api/auth/register") {
            setBody(dto)
        }

        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun refreshToken(dto: RefreshDTO): ServerResponse<TokenDTO> {
        val response = client.post("/api/auth/refresh") {
            setBody(dto)
        }

        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun logout(dto: LogoutDTO): Result<Unit> {
        return runCatching {
            client.post("/api/auth/logout") {
                setBody(dto)
            }
        }
    }
}