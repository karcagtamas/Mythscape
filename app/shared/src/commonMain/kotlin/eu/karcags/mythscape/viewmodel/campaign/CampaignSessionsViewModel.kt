package eu.karcags.mythscape.viewmodel.campaign

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.dtos.sessions.SessionDTO
import eu.karcags.mythscape.dtos.sessions.SessionRequestDTO
import eu.karcags.mythscape.network.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CampaignSessionsViewModel(
    private val repository: SessionRepository,
) : ViewModel() {

    private val _sessions = MutableStateFlow<List<SessionDTO>>(emptyList())
    val sessions: StateFlow<List<SessionDTO>> = _sessions

    var isLoading by mutableStateOf(false)
    var currentPage by mutableStateOf(0)
    var isLastPage by mutableStateOf(false)

    var showFormDialog by mutableStateOf(false)
    var editingSession by mutableStateOf<SessionDTO?>(null)
    var isProcessing by mutableStateOf(false)
    val errorMessage by mutableStateOf<String?>(null)

    private var activeCampaignId: Int = 0
    private val pageSize = 15

    fun initialize(campaignId: Int) {
        if (activeCampaignId == campaignId) return
        activeCampaignId = campaignId
        resetPaging()
        loadNextPage()
    }

    fun loadNextPage() {
        if (isLoading || isLastPage) return
        isLoading = true

        viewModelScope.launch {
            try {
                val res = repository.getSessions(activeCampaignId, page = currentPage, size = pageSize)
                if (res.success && !res.data.isNullOrEmpty()) {
                    _sessions.value += res.data!!
                    currentPage++
                    if (res.data!!.size < pageSize) isLastPage = true
                } else {
                    isLastPage = false
                }
            } catch (e: Exception) {

            } finally {
                isLoading = false
            }
        }
    }

    fun resetPaging() {
        _sessions.value = emptyList()
        currentPage = 0
        isLastPage = false
    }

    fun openCreateDialog() {
        editingSession = null
        showFormDialog = true
    }

    fun openEditDialog(session: SessionDTO) {
        editingSession = session
        showFormDialog = true
    }

    fun saveSession(dto: SessionRequestDTO) {
        isProcessing = true
        viewModelScope.launch {
            try {
                val current = editingSession

                val res = if (current == null) {
                    repository.createSession(dto)
                } else {
                    repository.updateSession(current.id, dto)
                }

                if (res.success) {
                    showFormDialog = false
                    resetPaging()
                    loadNextPage()
                }
            } catch (e: Exception) {

            } finally {
                isProcessing = false
            }
        }
    }

    fun toggleCanceledState(session: SessionDTO) {
        viewModelScope.launch {
            val dto = SessionRequestDTO(
                session.date,
                session.startTime,
                session.endTime,
                activeCampaignId,
                !session.canceled,
            )

            if (repository.updateSession(session.id, dto).success) {
                _sessions.value = _sessions.value.map {
                    if (it.id == session.id) it.copy(canceled = !it.canceled) else it
                }
            }
        }
    }

    fun deleteSession(session: SessionDTO) {
        viewModelScope.launch {
            if (repository.deleteSession(session.id).success) {
                _sessions.value -= session
            }
        }
    }
}