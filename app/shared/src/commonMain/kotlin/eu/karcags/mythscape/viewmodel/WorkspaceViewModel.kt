package eu.karcags.mythscape.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.common.SessionManager
import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import eu.karcags.mythscape.network.CampaignRepository
import eu.karcags.mythscape.enums.WorkspaceScreenState
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

    var state by mutableStateOf<WorkspaceScreenState>(WorkspaceScreenState.Dashboard)
        private set

    init {
        loadUserCampaigns()
    }

    fun selectDashboard() {
        updateState(WorkspaceScreenState.Dashboard)
    }

    fun selectProfile() {
        updateState(WorkspaceScreenState.Profile)
    }

    fun selectCampaign(campaign: CampaignDTO) {
        updateState(WorkspaceScreenState.CampaignDashboard(campaign))
    }

    fun selectCampaignCreate() {
        updateState(WorkspaceScreenState.CampaignCreate)
    }

    fun selectCampaignEdit(campaign: CampaignDTO) {
        updateState(WorkspaceScreenState.CampaignEdit(campaign))
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

    private fun updateState(state: WorkspaceScreenState) {
        this.state = state
    }
}