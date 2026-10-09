package eu.karcags.mythscape.viewmodel.calendar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.dtos.sessions.SessionDTO
import eu.karcags.mythscape.network.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class AgendaViewModel(private val repository: SessionRepository) : ViewModel() {

    private val _sessions = MutableStateFlow<List<SessionDTO>>(emptyList())
    val sessions: StateFlow<List<SessionDTO>> = _sessions

    var isLoading by mutableStateOf(false)
        private set

    fun loadAgenda(date: LocalDate) {
        isLoading = true

        viewModelScope.launch {
            try {
                val res = repository.getAgenda(date)
                if (res.success && res.data != null) {
                    _sessions.value = res.data!!
                }
            } catch (e: Exception) {
                _sessions.value = emptyList()
            } finally {
                isLoading = false
            }
        }
    }
}