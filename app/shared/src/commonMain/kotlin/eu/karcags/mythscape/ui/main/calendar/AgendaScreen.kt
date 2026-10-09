package eu.karcags.mythscape.ui.main.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.ui.components.common.LoadingBox
import eu.karcags.mythscape.viewmodel.calendar.AgendaViewModel
import kotlinx.datetime.LocalDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AgendaScreen(viewModel: AgendaViewModel = koinViewModel(), today: LocalDate) {
    val sessions by viewModel.sessions.collectAsState()

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
                items(sessions) { session ->
                    val isPast = session.date < today

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (session.canceled) MaterialTheme.colorScheme.error.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface,
                                shape = MaterialTheme.shapes.small,
                            )
                            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = "Session #${session.id}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPast) Color.Gray else Color.White,
                                )
                                if (session.canceled) {
                                    Text(
                                        text = "Canceled",
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier
                                            .border(1.dp, MaterialTheme.colorScheme.error, MaterialTheme.shapes.small)
                                            .padding(horizontal = 4.dp, vertical = 1.dp),
                                    )
                                }
                            }
                            Text(
                                text = "Timeline: ${session.date} [${session.startTime}-${session.endTime}]",
                                color = Color.LightGray,
                                fontSize = 11.sp,
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(
                                    if (session.canceled) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                ),
                        )
                    }
                }
            }
        }
    }
}