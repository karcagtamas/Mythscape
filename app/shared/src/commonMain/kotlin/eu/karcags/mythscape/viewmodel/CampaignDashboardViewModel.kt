package eu.karcags.mythscape.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignMemberDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignRequestDTO
import eu.karcags.mythscape.dtos.sessions.SessionDTO
import eu.karcags.mythscape.network.CampaignRepository
import eu.karcags.mythscape.network.SessionRepository
import kotlinx.coroutines.launch

class CampaignDashboardViewModel(
    private val campaignRepository: CampaignRepository,
    private val sessionRepository: SessionRepository,
) : ViewModel() {
    var members by mutableStateOf<List<CampaignMemberDTO>>(emptyList())
    var recentSessions by mutableStateOf<List<SessionDTO>>(emptyList())

    var isLoading by mutableStateOf(false)
    var isProcessing by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var showEditDialog by mutableStateOf(false)
        private set
    var isEditingProcessing by mutableStateOf(false)
        private set
    var editDialogError by mutableStateOf<String?>(null)
        private set

    fun openEditDialog() {
        showEditDialog = true
    }

    fun closeEditDialog() {
        showEditDialog = false
    }

    fun initialize(campaignId: Int) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val membersRes = campaignRepository.getCampaignMembers(campaignId)
                val sessionsRes = sessionRepository.getRecentSessions(campaignId)

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

    fun edit(campaignId: Int, dto: CampaignRequestDTO, onUpdate: (CampaignDTO) -> Unit) {
        isEditingProcessing = true
        editDialogError = null
        viewModelScope.launch {
            try {
                val response = campaignRepository.updateCampaign(campaignId, dto)
                if (response.success && response.data != null) {
                    showEditDialog = false
                    initialize(campaignId)
                    onUpdate(response.data!!)
                } else {
                    editDialogError = response.error?.message ?: "Unknown error"
                }
            } catch (e: Exception) {
                editDialogError = e.message ?: "Unknown error"
            } finally {
                isEditingProcessing = false
            }
        }
    }

    fun archive(campaignId: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            isProcessing = true
            try {
                val res = campaignRepository.archiveCampaign(campaignId)
                if (res.success) onComplete()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Failed to execute realm archive operation."
            } finally {
                isProcessing = false
            }
        }
    }

    fun delete(campaignId: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            isProcessing = true
            try {
                val res = campaignRepository.deleteCampaign(campaignId)
                if (res.success) onComplete()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Failed to purge campaign realm from database."
            } finally {
                isProcessing = false
            }
        }
    }
}