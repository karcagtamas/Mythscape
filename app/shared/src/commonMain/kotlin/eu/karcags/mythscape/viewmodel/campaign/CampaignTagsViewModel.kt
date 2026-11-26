package eu.karcags.mythscape.viewmodel.campaign

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.core.dtos.campaigns.CampaignTagDTO
import eu.karcags.mythscape.core.dtos.campaigns.CampaignTagRequestDTO
import eu.karcags.mythscape.network.CampaignRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CampaignTagsViewModel(
    private val repository: CampaignRepository,
) : ViewModel() {

    private val _tags = MutableStateFlow<List<CampaignTagDTO>>(emptyList())
    val tags: StateFlow<List<CampaignTagDTO>> = _tags

    var isLoading by mutableStateOf(false)

    var showFormDialog by mutableStateOf(false)
    var editing by mutableStateOf<CampaignTagDTO?>(null)
    var isProcessing by mutableStateOf(false)
    val errorMessage by mutableStateOf<String?>(null)

    private var activeCampaignId: Int = 0

    fun initialize(campaignId: Int) {
        if (activeCampaignId == campaignId) return
        activeCampaignId = campaignId
        load()
    }

    fun load() {
        if (isLoading) return
        isLoading = true

        viewModelScope.launch {
            try {
                val res =
                    repository.getCampaignTags(activeCampaignId)
                if (res.success && !res.data.isNullOrEmpty()) {
                    _tags.value = res.data!!
                } else {
                    _tags.value = emptyList()
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    ensureActive()
                }
            } finally {
                isLoading = false
            }
        }
    }

    fun openCreateDialog() {
        editing = null
        showFormDialog = true
    }

    fun openEditDialog(tag: CampaignTagDTO) {
        editing = tag
        showFormDialog = true
    }

    fun save(dto: CampaignTagRequestDTO) {
        isProcessing = true
        viewModelScope.launch {
            try {
                val current = editing

                val res = if (current == null) {
                    repository.createCampaignTag(activeCampaignId, dto)
                } else {
                    repository.updateCampaignTag(current.id, dto)
                }

                if (res.success) {
                    showFormDialog = false
                    load()
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    ensureActive()
                }
            } finally {
                isProcessing = false
            }
        }
    }

    fun delete(tag: CampaignTagDTO) {
        viewModelScope.launch {
            if (repository.deleteCampaignTag(tag.id).success) {
                _tags.value -= tag
            }
        }
    }
}