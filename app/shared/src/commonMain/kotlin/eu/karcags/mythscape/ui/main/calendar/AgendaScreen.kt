package eu.karcags.mythscape.ui.main.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.ui.components.common.HorizontalLine
import eu.karcags.mythscape.ui.components.common.LoadingBox
import eu.karcags.mythscape.viewmodel.calendar.AgendaViewModel
import kotlinx.datetime.LocalDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AgendaScreen(viewModel: AgendaViewModel = koinViewModel(), today: LocalDate) {
    val sessions by viewModel.sessions.collectAsState()

    LaunchedEffect(today) {
        viewModel.loadAgenda(today)
    }

    val groupedByDay = remember(sessions) {
        sessions.sortedBy { it.date }.groupBy { it.date }
    }

    LoadingBox(
        isLoading = viewModel.isLoading,
    ) {
        if (sessions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("No play session chronicle written for the next 30 days.")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                groupedByDay.forEach { (date, daySessions) ->
                    stickyHeader {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = "${date.dayOfWeek.name.take(3)} // $date",
                                color = if (date == today) MaterialTheme.colorScheme.primary else Color.Gray,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                            )

                            if (date == today) {
                                Text("TODAY", color = MaterialTheme.colorScheme.primary)
                            }
                            HorizontalLine(0.25f)
                        }
                    }

                    items(daySessions) { session ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = if (session.canceled) MaterialTheme.colorScheme.error.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface,
                                    shape = MaterialTheme.shapes.small,
                                )
                                .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Text(
                                        "Session #${session.id}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (session.canceled) {
                                        Text(
                                            text = "CANCELED",
                                            color = MaterialTheme.colorScheme.error,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Black,
                                            modifier = Modifier
                                                .border(
                                                    1.dp,
                                                    MaterialTheme.colorScheme.error,
                                                    MaterialTheme.shapes.small,
                                                )
                                                .padding(horizontal = 4.dp, vertical = 1.dp),
                                        )
                                    }
                                }
                                Text("${session.startTime} - ${session.endTime}", color = Color.Gray, fontSize = 11.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(if (session.canceled) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary),
                            )
                        }
                    }
                }
            }
        }
    }
}