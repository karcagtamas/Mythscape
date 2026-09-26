package eu.karcags.mythscape.viewmodel.campaign

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import eu.karcags.mythscape.enums.CampaignWorkspaceScreenState
import eu.karcags.mythscape.network.CampaignRepository
import kotlinx.coroutines.launch

class CampaignWorkspaceViewModel(
    private val campaignRepository: CampaignRepository,
) : ViewModel() {

    var campaign by mutableStateOf<CampaignDTO?>(null)
    var screenState by mutableStateOf<CampaignWorkspaceScreenState>(CampaignWorkspaceScreenState.Dashboard)
        private set

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun initialize(campaignId: Int) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val campaignRes = campaignRepository.getCampaign(campaignId)

                if (campaignRes.success && campaignRes.data != null) {
                    campaign = campaignRes.data
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Something went wrong"
            } finally {
                isLoading = false
            }
        }
    }

    fun selectTab(targetState: CampaignWorkspaceScreenState) {
        screenState = targetState
    }

    fun resetToDashBoard() {
        screenState = CampaignWorkspaceScreenState.Dashboard
    }
}