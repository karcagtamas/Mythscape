package eu.karcags.mythscape.network

import eu.karcags.mythscape.ServerResponse
import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CampaignRepository(private val baseUrl: String = "http://localhost:8080/api") {

    private val client by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    suspend fun fetchCampaigns(userId: Int, token: String): ServerResponse<List<CampaignDTO>> {
        val response = client.get("$baseUrl/campaigns/users/$userId") {
            header(HttpHeaders.Authorization, "Bearer $token")
            accept(ContentType.Application.Json)
        }
        return Json.decodeFromString(response.bodyAsText())
    }
}