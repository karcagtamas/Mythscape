package eu.karcags.mythscape.network

import eu.karcags.mythscape.ServerResponse
import eu.karcags.mythscape.dtos.auth.LoginDTO
import eu.karcags.mythscape.dtos.auth.RegisterDTO
import eu.karcags.mythscape.dtos.auth.TokenDTO
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class AuthRepository {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun login(dto: LoginDTO): ServerResponse<TokenDTO> {
        val response = client.post("http://localhost:8080/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun register(dto: RegisterDTO): ServerResponse<Int> {
        val response = client.post("http://localhost:8080/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        return Json.decodeFromString(response.bodyAsText())
    }
}