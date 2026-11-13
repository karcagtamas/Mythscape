package eu.karcags.mythscape.ui.main.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.ui.components.common.AppButton
import eu.karcags.mythscape.ui.components.common.HorizontalLine
import eu.karcags.mythscape.ui.components.common.LoadingBox
import eu.karcags.mythscape.viewmodel.calendar.WeeklyCalendarViewModel
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WeeklyCalendarViewScreen(
    viewModel: WeeklyCalendarViewModel = koinViewModel(),
    today: LocalDate,
) {
    val sessions by viewModel.sessions.collectAsState()
    val bounds = viewModel.calculateBounds()

    val weekDays = remember(bounds) {
        (0..6).map { bounds.first.plus(it, DateTimeUnit.DAY) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppButton(
                text = "<",
                onClick = { viewModel.stepBackward() },
                modifier = Modifier.width(32.dp),
            )
            Text(
                text = "${bounds.first} — ${bounds.second}",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(200.dp),
                textAlign = TextAlign.Center
            )
            AppButton(
                text = ">",
                onClick = { viewModel.stepForward() },
                modifier = Modifier.width(32.dp),
            )
        }

        LoadingBox(
            isLoading = viewModel.isLoading,
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                weekDays.forEach { date ->
                    val isToday = date == today
                    val daySessions = sessions.filter { it.date == date }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(
                                color = if (isToday) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface,
                                shape = MaterialTheme.shapes.small,
                            )
                            .border(
                                width = 1.dp,
                                color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.small,
                            )
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = date.dayOfWeek.name.take(3),
                                color = if (isToday) MaterialTheme.colorScheme.primary else Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "${date.day.toString().padStart(2, '0')} ${
                                    date.month.toString().padStart(2, '0')
                                }",
                                color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        HorizontalLine(alpha = 0.4f)

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            items(daySessions) { session ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = if (session.canceled) MaterialTheme.colorScheme.error.copy(alpha = 0.1f) else MaterialTheme.colorScheme.background,
                                            shape = MaterialTheme.shapes.small,
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (isToday) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary.copy(
                                                alpha = 0.5f
                                            ),
                                            shape = MaterialTheme.shapes.small,
                                        )
                                        .padding(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(2.dp),
                                ) {
                                    Text(
                                        text = "Session #${session.id}",
                                        color = if (session.canceled) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = "${session.startTime} - ${session.endTime}",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 10.sp
                                    )
                                    if (session.canceled) {
                                        Text(
                                            "CANCELED",
                                            color = MaterialTheme.colorScheme.error,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}