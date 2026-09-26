package eu.karcags.mythscape.network

import eu.karcags.mythscape.ServerResponse
import eu.karcags.mythscape.dtos.sessions.SessionDTO
import eu.karcags.mythscape.dtos.sessions.SessionRequestDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class SessionRepository(private val client: HttpClient) {
    suspend fun getSessions(
        campaignId: Int,
        page: Int,
        size: Int,
        showCanceled: Boolean = false
    ): ServerResponse<List<SessionDTO>> {
        val response = client.get("/api/sessions") {
            url {
                parameter("campaignId", campaignId)
                parameter("page", page)
                parameter("size", size)
                parameter("showCanceled", showCanceled)
            }
        }
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun getRecentSessions(campaignId: Int, number: Int = 3): ServerResponse<List<SessionDTO>> {
        val response = client.get("/api/sessions/recent") {
            url {
                parameter("campaignId", campaignId)
                parameter("number", number)
            }
        }
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun createSession(dto: SessionRequestDTO): ServerResponse<SessionDTO> {
        val response = client.post("/api/sessions") { setBody(dto) }
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun updateSession(id: Int, dto: SessionRequestDTO): ServerResponse<SessionDTO> {
        val response = client.put("/api/sessions/$id") { setBody(dto) }
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun deleteSession(id: Int): ServerResponse<Unit> {
        val response = client.delete("/api/sessions/$id")
        return Json.decodeFromString(response.bodyAsText())
    }
}