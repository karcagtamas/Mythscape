package eu.karcags.mythscape.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import eu.karcags.mythscape.enums.CampaignWorkspaceScreenState

class CampaignWorkspaceViewModel : ViewModel() {

    var screenState by mutableStateOf<CampaignWorkspaceScreenState>(CampaignWorkspaceScreenState.Dashboard)
        private set

    fun selectTab(targetState: CampaignWorkspaceScreenState) {
        screenState = targetState
    }

    fun resetToDashBoard() {
        screenState = CampaignWorkspaceScreenState.Dashboard
    }
}