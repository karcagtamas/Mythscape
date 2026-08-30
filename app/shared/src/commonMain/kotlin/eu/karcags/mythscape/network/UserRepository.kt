package eu.karcags.mythscape.network

import eu.karcags.mythscape.ServerResponse
import eu.karcags.mythscape.dtos.UserDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class UserRepository(private val client: HttpClient) {

    suspend fun getCurrentUser(): ServerResponse<UserDTO> {
        val response = client.get("/api/users/current")
        return Json.decodeFromString(response.bodyAsText())
    }
}