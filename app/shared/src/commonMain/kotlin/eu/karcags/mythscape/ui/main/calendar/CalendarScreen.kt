package eu.karcags.mythscape.ui.main.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import eu.karcags.mythscape.enums.CalendarViewMode
import eu.karcags.mythscape.viewmodel.calendar.CalendarViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = koinViewModel(),
) {
    val today = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }

    when (viewModel.viewMode) {
        CalendarViewMode.WEEK -> {

        }
        CalendarViewMode.MONTH -> {

        }
        CalendarViewMode.AGENDA -> {
            AgendaScreen(today = today)
        }
    }
}