package eu.karcags.mythscape.ui.main.campaign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.ui.components.common.AppButton
import eu.karcags.mythscape.ui.components.common.HorizontalLine
import eu.karcags.mythscape.ui.components.dialogs.campaign.SessionFormDialog
import eu.karcags.mythscape.viewmodel.campaign.CampaignSessionsViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
fun CampaignSessionsScreen(
    campaignId: Int,
    viewModel: CampaignSessionsViewModel = koinViewModel(),
) {
    LaunchedEffect(campaignId) {
        viewModel.initialize(campaignId)
    }

    val sessions by viewModel.sessions.collectAsState()
    val today = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Campaign Sessions",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
                Box(
                    modifier = Modifier.width(140.dp),
                ) {
                    AppButton(
                        text = "Add",
                        onClick = { viewModel.openCreateDialog() }
                    )
                }
            }

            HorizontalLine(0.3f)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                itemsIndexed(sessions) { index, session ->
                    if (index >= sessions.size - 2 && !viewModel.isLastPage) {
                        LaunchedEffect(Unit) {
                            viewModel.loadNextPage()
                        }
                    }

                    val isPast = session.date < today.date
                    val backgroundTint = if (session.canceled) {
                        MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                    } else if (isPast) {
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(backgroundTint, shape = MaterialTheme.shapes.small)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = if (isPast) 0.3f else 1f),
                                MaterialTheme.shapes.small
                            )
                            .padding(12.dp),
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
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isPast) Color.Gray else Color.White
                                )

                                if (session.canceled) {
                                    Text(
                                        "Canceled",
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier
                                            .border(
                                                1.dp,
                                                MaterialTheme.colorScheme.error,
                                                MaterialTheme.shapes.small
                                            )
                                            .padding(horizontal = 4.dp, vertical = 1.dp),
                                    )
                                } else if (isPast) {
                                    Text(
                                        "Past entry",
                                        color = Color.DarkGray,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                            Text(
                                text = "${session.date} @ ${session.startTime} - ${session.endTime}",
                                color = if (isPast) Color.DarkGray else Color.LightGray,
                                fontSize = 12.sp,
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .width(280.dp),
                        ) {
                            Box(modifier = Modifier.weight(1.2f)) {
                                AppButton(
                                    text = if (session.canceled) "Re-schedule" else "Cancel",
                                    onClick = { viewModel.toggleCanceledState(session) },
                                )
                            }

                            Box(modifier = Modifier.weight(0.8f)) {
                                AppButton(
                                    text = "Edit",
                                    onClick = { viewModel.openEditDialog(session) },
                                )
                            }

                            Box(modifier = Modifier.weight(0.8f)) {
                                AppButton(
                                    text = "Delete",
                                    onClick = { viewModel.deleteSession(session) },
                                )
                            }
                        }
                    }
                }
            }
        }

        if (viewModel.showFormDialog) {
            SessionFormDialog(
                campaignId = campaignId,
                session = viewModel.editingSession,
                isProcessing = viewModel.isProcessing,
                onDismiss = { viewModel.showFormDialog = false },
                onConfirm = { viewModel.saveSession(it) },
            )
        }
    }
}