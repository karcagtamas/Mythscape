package eu.karcags.mythscape.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignMemberDTO
import eu.karcags.mythscape.dtos.sessions.SessionDTO
import eu.karcags.mythscape.network.CampaignRepository
import eu.karcags.mythscape.network.SessionRepository
import kotlinx.coroutines.launch

class CampaignDashboardViewModel(
    private val campaignRepository: CampaignRepository,
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    var campaign by mutableStateOf<CampaignDTO?>(null)
    var members by mutableStateOf<List<CampaignMemberDTO>>(emptyList())
    var recentSessions by mutableStateOf<List<SessionDTO>>(emptyList())

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun initialize(campaignId: Int) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val campaignRes = campaignRepository.getCampaign(campaignId)
                val membersRes = campaignRepository.getCampaignMembers(campaignId)
                val sessionsRes = sessionRepository.getRecentSessions(campaignId)

                if (campaignRes.success && campaignRes.data != null) {
                    campaign = campaignRes.data
                }

                if (membersRes.success && membersRes.data != null) {
                    members = membersRes.data!!
                }

                if (sessionsRes.success && sessionsRes.data != null) {
                    recentSessions = sessionsRes.data!!
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Something went wrong"
            } finally {
                isLoading = false
            }
        }
    }
}