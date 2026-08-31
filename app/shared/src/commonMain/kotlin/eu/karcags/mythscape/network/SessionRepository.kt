package eu.karcags.mythscape.network

import eu.karcags.mythscape.ServerResponse
import eu.karcags.mythscape.dtos.sessions.SessionDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class SessionRepository(private val client: HttpClient) {
    suspend fun getSessions(campaignId: Int, showAll: Boolean = false): ServerResponse<List<SessionDTO>> {
        val response = client.get("/api/sessions") {
            url {
                parameter("campaignId", campaignId)
                parameter("showAll", showAll)
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
}