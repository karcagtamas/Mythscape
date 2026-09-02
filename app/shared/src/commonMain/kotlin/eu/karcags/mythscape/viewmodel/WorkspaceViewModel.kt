package eu.karcags.mythscape.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.common.SessionManager
import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import eu.karcags.mythscape.network.CampaignRepository
import eu.karcags.mythscape.enums.WorkspaceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkspaceViewModel(
    private val sessionManager: SessionManager,
    private val campaignRepository: CampaignRepository,
) : ViewModel() {

    private val _campaigns = MutableStateFlow<List<CampaignDTO>>(emptyList())
    val campaigns: StateFlow<List<CampaignDTO>> = _campaigns

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    var activeView by mutableStateOf(WorkspaceState.DASHBOARD)
        private set
    var selectedCampaign by mutableStateOf<CampaignDTO?>(null)
        private set

    init {
        loadUserCampaigns()
    }

    fun selectDashboard() {
        activeView = WorkspaceState.DASHBOARD
        selectedCampaign = null
    }

    fun selectProfile() {
        activeView = WorkspaceState.PROFILE
        selectedCampaign = null
    }

    fun selectCampaign(campaign: CampaignDTO) {
        selectedCampaign = campaign
        activeView = WorkspaceState.CAMPAIGN_DASHBOARD
    }

    fun selectCampaignCreate() {
        activeView = WorkspaceState.CAMPAIGN_CREATE
    }

    fun selectCampaignEdit(campaign: CampaignDTO) {
        selectedCampaign = campaign
        activeView = WorkspaceState.CAMPAIGN_EDIT
    }

    fun loadUserCampaigns() {
        val userId = sessionManager.getUserId()

        if (userId == 0) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = campaignRepository.fetchCampaigns(userId)
                if (response.success && response.data != null) {
                    _campaigns.value = response.data!!
                }
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }
}