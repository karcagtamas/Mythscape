package eu.karcags.mythscape.ui.main.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.dtos.sessions.SessionDTO
import eu.karcags.mythscape.ui.components.common.AppButton
import eu.karcags.mythscape.ui.components.common.LoadingBox
import eu.karcags.mythscape.ui.components.main.calendar.CalendarNavigator
import eu.karcags.mythscape.viewmodel.calendar.MonthlyCalendarViewModel
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MonthlyCalendarViewScreen(
    viewModel: MonthlyCalendarViewModel = koinViewModel(),
    today: LocalDate,
) {
    val sessions by viewModel.sessions.collectAsState()
    val daysOfWeek = remember { DayOfWeek.entries.map { it.name.take(3) } }
    val bounds = viewModel.calculateBounds()

    val totalGridDays = remember(bounds) {
        val list = mutableListOf<LocalDate>()
        var current = bounds.first
        while (current <= bounds.second) {
            list.add(current)
            current = current.plus(1, DateTimeUnit.DAY)
        }
        list.toList()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CalendarNavigator(
            text = "${viewModel.anchor.month.name} ${viewModel.anchor.year}",
            back = { viewModel.stepBackward() },
            forward = { viewModel.stepForward() }
        )

        LoadingBox(
            isLoading = viewModel.isLoading,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter,
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 1440.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Header(daysOfWeek)

                    Grid(totalGridDays, sessions, today, viewModel.anchor)
                }
            }
        }
    }
}

@Composable
private fun Header(
    daysOfWeek: List<String>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.small
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        daysOfWeek.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun Grid(
    days: List<LocalDate>,
    sessions: List<SessionDTO>,
    today: LocalDate,
    anchor: LocalDate,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(days) { date ->
            val isCurrentMonth = date.month == anchor.month
            val isToday = date == today
            val daySessions = sessions.filter { it.date == date }

            Cell(date, isToday, isCurrentMonth, daySessions)
        }
    }
}

@Composable
private fun Cell(
    date: LocalDate,
    isToday: Boolean,
    isCurrentMonth: Boolean,
    sessions: List<SessionDTO>,
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(
                color = when {
                    isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                    isCurrentMonth -> MaterialTheme.colorScheme.surface
                    else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                },
                shape = MaterialTheme.shapes.small,
            )
            .border(
                width = 1.dp,
                color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(
                    alpha = if (isCurrentMonth) 1f else 0.3f
                ),
                shape = MaterialTheme.shapes.small,
            )
            .padding(4.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                text = date.day.toString(),
                color = when {
                    isToday -> MaterialTheme.colorScheme.primary
                    isCurrentMonth -> MaterialTheme.colorScheme.onSurface
                    else -> Color.DarkGray
                },
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(2.dp))
            sessions.take(2).forEach { session ->
                SessionRow(session)
            }
            if (sessions.size > 2) {
                Text("+${sessions.size - 2}", color = Color.Gray, fontSize = 8.sp)
            }
        }
    }
}

@Composable
private fun SessionRow(
    session: SessionDTO,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp)
            .background(
                color = if (session.canceled) MaterialTheme.colorScheme.error.copy(
                    alpha = 0.15f
                )
                else MaterialTheme.colorScheme.primary.copy(0.2f),
                shape = MaterialTheme.shapes.small,
            )
            .padding(horizontal = 4.dp, vertical = 1.dp),
    ) {
        Text(
            text = if (session.canceled) "CANCELED" else "#${session.id} ${session.startTime}",
            color = if (session.canceled) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            fontSize = 8.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}