package eu.karcags.mythscape.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignEditDTO
import eu.karcags.mythscape.enums.FormState
import eu.karcags.mythscape.network.CampaignRepository
import kotlinx.coroutines.launch

class CampaignFormViewModel(
    private val repository: CampaignRepository,
) : ViewModel() {

    private var targetCampaignId: Int? = null

    var mode by mutableStateOf(FormState.CREATE)
        private set

    var name by mutableStateOf("")
    var title by mutableStateOf("")
    var description by mutableStateOf<String?>(null)

    var isProcessing by mutableStateOf(false)
    var fieldError by mutableStateOf<String?>(null)

    fun setup(mode: FormState, campaign: CampaignDTO? = null) {
        this.mode = mode
        fieldError = null

        if (mode == FormState.EDIT && campaign != null) {
            targetCampaignId = campaign.id
            name = campaign.name
            title = campaign.name
            description = campaign.description
        } else {
            targetCampaignId = null
            name = ""
            title = ""
            description = ""
        }
    }

    fun submit(onSuccess: () -> Unit) {
        if (name.isBlank() || title.isBlank()) {
            fieldError = "Name and title fields are mandatory"
            return
        }

        isProcessing = true
        fieldError = null

        viewModelScope.launch {
            try {
                val dto = CampaignEditDTO(
                    name,
                    title,
                    description,
                )

                val response = if (mode == FormState.EDIT) {
                    repository.updateCampaign(targetCampaignId ?: 0, dto)
                } else {
                    repository.createCampaign(dto)
                }

                if (response.success) {
                    onSuccess()
                } else {
                    fieldError = response.error?.message ?: "Sever error."
                }
            } catch (e: Exception) {
                fieldError = e.message ?: "Network error."
            } finally {
                isProcessing = false
            }
        }
    }
}