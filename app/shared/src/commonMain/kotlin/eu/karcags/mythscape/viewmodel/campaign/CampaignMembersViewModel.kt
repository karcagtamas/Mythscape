package eu.karcags.mythscape.viewmodel.campaign

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.dtos.UserDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignMemberDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignMemberRequestDTO
import eu.karcags.mythscape.network.CampaignRepository
import eu.karcags.mythscape.network.UserRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CampaignMembersViewModel(
    private val repository: CampaignRepository,
) : ViewModel() {

    private val _members = MutableStateFlow<List<CampaignMemberDTO>>(emptyList())
    val members: StateFlow<List<CampaignMemberDTO>> = _members

    private val _users = MutableStateFlow<List<UserDTO>>(emptyList())
    val users: StateFlow<List<UserDTO>> = _users

    var isLoading by mutableStateOf(false)

    var showFormDialog by mutableStateOf(false)
    var editing by mutableStateOf<CampaignMemberDTO?>(null)
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
                loadMembers()
                loadUsers()
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

    fun openEditDialog(member: CampaignMemberDTO) {
        editing = member
        showFormDialog = true
    }

    fun save(dto: CampaignMemberRequestDTO) {
        isProcessing = true
        viewModelScope.launch {
            try {
                val current = editing

                val res = if (current == null) {
                    repository.createCampaignMember(activeCampaignId, dto)
                } else {
                    repository.updateCampaignMember(current.id, dto)
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

    fun delete(member: CampaignMemberDTO) {
        viewModelScope.launch {
            if (repository.deleteCampaignMember(member.id).success) {
                _members.value -= member
            }
        }
    }

    private suspend fun CoroutineScope.loadMembers() {
        val res = repository.getCampaignMembers(activeCampaignId)

        if (res.success && !res.data.isNullOrEmpty()) {
            _members.value = res.data!!
        } else {
            _members.value = emptyList()
        }
    }

    private suspend fun CoroutineScope.loadUsers() {
        val res = repository.getPossibleUsers(activeCampaignId)

        if (res.success && !res.data.isNullOrEmpty()) {
            _users.value = res.data!!
        } else {
            _users.value = emptyList()
        }
    }
}