package eu.karcags.mythscape.viewmodel

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

enum class CalendarViewMode { WEEK, MONTH }

class CalendarViewModel(private val repository: SessionRepository) : ViewModel() {

    private val _sessions = MutableStateFlow<List<SessionDTO>>(emptyList())
    val sessions: StateFlow<List<SessionDTO>> = _sessions

    var viewMode by mutableStateOf(CalendarViewMode.MONTH)
        private set

    var currentAnchorDate by mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadSessions()
    }

    fun switchViewMode(mode: CalendarViewMode) {
        viewMode = mode
        loadSessions()
    }

    fun stepForward() {
        currentAnchorDate = when (viewMode) {
            CalendarViewMode.WEEK -> currentAnchorDate.plus(7, DateTimeUnit.DAY)
            CalendarViewMode.MONTH -> currentAnchorDate.plus(1, DateTimeUnit.MONTH)
        }
        loadSessions()
    }

    fun stepBackward() {
        currentAnchorDate = when (viewMode) {
            CalendarViewMode.WEEK -> currentAnchorDate.minus(7, DateTimeUnit.DAY)
            CalendarViewMode.MONTH -> currentAnchorDate.minus(1, DateTimeUnit.MONTH)
        }
        loadSessions()
    }

    fun loadSessions() {
        isLoading = true
        val (start, end) = calculateDateRangeBounds()

        viewModelScope.launch {
            try {
                val res = repository.getSessions()

                if (res.success && res.data != null) {
                    _sessions.value = res.data!!
                } else {
                    _sessions.value = emptyList()
                }
            } catch (error: Throwable) {
                _sessions.value = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    fun getFormattedHeaderLabel(): String {
        return when (viewMode) {
            CalendarViewMode.WEEK -> "Week of ${calculateDateRangeBounds().first}"
            CalendarViewMode.MONTH -> "${currentAnchorDate.month.name} ${currentAnchorDate.year}"
        }
    }

    private fun calculateDateRangeBounds(): Pair<LocalDate, Pair<LocalDate, LocalDate>> {
        return when (viewMode) {
            CalendarViewMode.WEEK -> {
                val dayOfWeek = currentAnchorDate.dayOfWeek.isoDayNumber
                val monday = currentAnchorDate.minus(dayOfWeek - 1, DateTimeUnit.DAY)
                val sunday = monday.plus(6, DateTimeUnit.DAY)

                Pair(monday, sunday)
            }

            CalendarViewMode.MONTH -> {
                val firstDay = LocalDate(currentAnchorDate.year, currentAnchorDate.month, 1)
                val firstOfNextMonth = firstDay.plus(1, DateTimeUnit.MONTH)
                val lastDay = firstOfNextMonth.minus(1, DateTimeUnit.DAY)
                Pair(firstDay, lastDay)
            }
        }.let {
            Pair(it.first, it)
        }
    }
}