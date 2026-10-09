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
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class WeeklyCalendarViewModel(private val repository: SessionRepository) : ViewModel() {

    private val _sessions = MutableStateFlow<List<SessionDTO>>(emptyList())
    val sessions: StateFlow<List<SessionDTO>> = _sessions

    var anchor by mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadSessions()
    }

    fun stepForward() {
        anchor = anchor.plus(7, DateTimeUnit.DAY)
        loadSessions()
    }

    fun stepBackward() {
        anchor = anchor.minus(7, DateTimeUnit.DAY)
        loadSessions()
    }

    fun calculateBounds(): Pair<LocalDate, LocalDate> {
        val dayOfWeek = anchor.dayOfWeek.isoDayNumber
        val monday = anchor.minus(dayOfWeek - 1, DateTimeUnit.DAY)
        val sunday = monday.plus(6, DateTimeUnit.DAY)
        return Pair(monday, sunday)
    }

    private fun loadSessions() {
        isLoading = true
        val (start, end) = calculateBounds()

        viewModelScope.launch {
            try {
                val res = repository.getSessions(
                    after = start,
                    before = end,
                )
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