package eu.karcags.mythscape.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.common.SessionManager
import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignRequestDTO
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

    var isCreatingCampaign by mutableStateOf(false)
        private set
    var createCampaignError by mutableStateOf<String?>(null)
        private set
    var showCampaignCreateDialog by mutableStateOf(false)
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
        updateState(WorkspaceScreenState.CampaignWorkspace(campaign.id, campaign.title))
    }

    fun updateCampaign(title: String) {
        if (state is WorkspaceScreenState.CampaignWorkspace) {
            updateState(
                WorkspaceScreenState.CampaignWorkspace(
                    (state as WorkspaceScreenState.CampaignWorkspace).campaignId,
                    title
                )
            )
        }
    }

    fun openCampaignCreateDialog() {
        showCampaignCreateDialog = true
    }

    fun closeCampaignCreateDialog() {
        showCampaignCreateDialog = false
    }

    fun createCampaign(dto: CampaignRequestDTO) {
        isCreatingCampaign = true
        createCampaignError = null
        viewModelScope.launch {
            try {
                val response = campaignRepository.createCampaign(dto)
                if (response.success) {
                    loadUserCampaigns()
                    isCreatingCampaign = false
                } else {
                    createCampaignError = response.error?.message ?: "Unknown error"
                }
            } catch (e: Exception) {
                createCampaignError = e.message ?: "Unknown error"
            } finally {
                isCreatingCampaign = false
            }
        }
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