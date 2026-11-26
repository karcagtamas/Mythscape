package eu.karcags.mythscape.viewmodel.calendar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import eu.karcags.mythscape.enums.CalendarViewMode

class CalendarViewModel : ViewModel() {
    var viewMode by mutableStateOf(CalendarViewMode.MONTH)
        private set

    fun switchViewMode(mode: CalendarViewMode) {
        viewMode = mode
    }
}