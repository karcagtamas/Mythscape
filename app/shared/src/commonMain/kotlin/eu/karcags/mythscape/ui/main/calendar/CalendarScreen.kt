package eu.karcags.mythscape.ui.main.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import eu.karcags.mythscape.enums.CalendarViewMode
import eu.karcags.mythscape.enums.ColorVariant
import eu.karcags.mythscape.ui.components.common.AppButton
import eu.karcags.mythscape.ui.components.common.Page
import eu.karcags.mythscape.ui.components.common.PageHeader
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

    Page {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PageHeader(
                text = "Calendar",
            ) {
                CalendarViewMode.entries
                    .forEach { viewMode ->
                        AppButton(
                            text = viewMode.name,
                            color = if (viewModel.viewMode == viewMode) ColorVariant.Primary else ColorVariant.Background,
                            onClick = { viewModel.switchViewMode(viewMode) },
                            modifier = Modifier
                                .width(80.dp),
                        )
                    }
            }

            when (viewModel.viewMode) {
                CalendarViewMode.WEEK -> {
                    AgendaScreen(today = today)
                }

                CalendarViewMode.MONTH -> {
                    AgendaScreen(today = today)
                }

                CalendarViewMode.AGENDA -> {
                    AgendaScreen(today = today)
                }
            }
        }
    }
}