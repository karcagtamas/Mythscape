package eu.karcags.mythscape.network

import eu.karcags.mythscape.ServerResponse
import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json

class CampaignRepository(private val client: HttpClient) {
    suspend fun fetchCampaigns(userId: Int): ServerResponse<List<CampaignDTO>> {
        val response = client.get("/api/campaigns/user/$userId")
        return Json.decodeFromString(response.bodyAsText())
    }
}