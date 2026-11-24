package eu.karcags.mythscape.network

import eu.karcags.mythscape.ServerResponse
import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignRequestDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignMemberDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignMemberRequestDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignTagDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignTagRequestDTO
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json

class CampaignRepository(private val client: HttpClient) {
    suspend fun fetchCampaigns(userId: Int): ServerResponse<List<CampaignDTO>> {
        val response = client.get("/api/campaigns/user/$userId")
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun getCampaign(id: Int): ServerResponse<CampaignDTO> {
        val response = client.get("/api/campaigns/$id")
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun createCampaign(dto: CampaignRequestDTO): ServerResponse<CampaignDTO> {
        val response = client.post("/api/campaigns") {
            setBody(dto)
        }
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun updateCampaign(id: Int, dto: CampaignRequestDTO): ServerResponse<CampaignDTO> {
        val response = client.put("/api/campaigns/${id}") {
            setBody(dto)
        }
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun deleteCampaign(id: Int): ServerResponse<Unit> {
        val response = client.delete("/api/campaigns/$id")
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun archiveCampaign(id: Int): ServerResponse<Unit> {
        val response = client.put("/api/campaigns/$id/archive")
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun getCampaignMembers(id: Int): ServerResponse<List<CampaignMemberDTO>> {
        val response = client.get("/api/campaigns/$id/members")
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun getCampaignTags(id: Int): ServerResponse<List<CampaignTagDTO>> {
        val response = client.get("/api/campaigns/$id/tags")
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun createCampaignTag(id: Int, dto: CampaignTagRequestDTO): ServerResponse<CampaignTagDTO> {
        val response = client.post("/api/campaigns/${id}/tags") {
            setBody(dto)
        }
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun updateCampaignTag(tagId: Int, dto: CampaignTagRequestDTO): ServerResponse<CampaignTagDTO> {
        val response = client.put("/api/campaigns/tags/${tagId}") {
            setBody(dto)
        }
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun deleteCampaignTag(tagId: Int): ServerResponse<Unit> {
        val response = client.delete("/api/campaigns/tags/$tagId")
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun createCampaignMember(id: Int, dto: CampaignMemberRequestDTO): ServerResponse<CampaignMemberDTO> {
        val response = client.post("/api/campaigns/${id}/members") {
            setBody(dto)
        }
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun updateCampaignMember(memberId: Int, dto: CampaignMemberRequestDTO): ServerResponse<CampaignMemberDTO> {
        val response = client.put("/api/campaigns/members/${memberId}") {
            setBody(dto)
        }
        return Json.decodeFromString(response.bodyAsText())
    }

    suspend fun deleteCampaignMember(memberId: Int): ServerResponse<Unit> {
        val response = client.delete("/api/campaigns/members/$memberId")
        return Json.decodeFromString(response.bodyAsText())
    }
}