package eu.karcags.mythscape.viewmodel.calendar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.dtos.sessions.SessionDTO
import eu.karcags.mythscape.enums.WorkspaceScreenState
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

class MonthlyCalendarViewModel(private val repository: SessionRepository) : ViewModel() {

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
        anchor = anchor.plus(1, DateTimeUnit.MONTH)
        loadSessions()
    }

    fun stepBackward() {
        anchor = anchor.minus(1, DateTimeUnit.MONTH)
        loadSessions()
    }

    fun calculateBounds(): Pair<LocalDate, LocalDate> {
        val firstOfMonth = LocalDate(anchor.year, anchor.month, 1)
        val dayOfWeek = firstOfMonth.dayOfWeek.isoDayNumber
        val gridStart = firstOfMonth.minus(dayOfWeek - 1, DateTimeUnit.DAY)

        val lastOfMonth = firstOfMonth.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
        val remainingDays = 7 - lastOfMonth.dayOfWeek.isoDayNumber
        val gridEnd = lastOfMonth.plus(remainingDays, DateTimeUnit.DAY)

        return Pair(gridStart, gridEnd)
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