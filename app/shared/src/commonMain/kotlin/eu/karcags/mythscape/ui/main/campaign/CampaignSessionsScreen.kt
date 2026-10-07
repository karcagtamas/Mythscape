package eu.karcags.mythscape.ui.main.campaign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.dtos.sessions.SessionDTO
import eu.karcags.mythscape.enums.ColorVariant
import eu.karcags.mythscape.theme.SmallIconSize
import eu.karcags.mythscape.ui.components.common.AppButton
import eu.karcags.mythscape.ui.components.common.HorizontalLine
import eu.karcags.mythscape.ui.components.common.InfoBox
import eu.karcags.mythscape.ui.components.common.Page
import eu.karcags.mythscape.ui.components.common.PageHeader
import eu.karcags.mythscape.ui.components.dialogs.campaign.SessionFormDialog
import eu.karcags.mythscape.viewmodel.campaign.CampaignSessionsViewModel
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mythscape.app.shared.generated.resources.Res
import mythscape.app.shared.generated.resources.ic_add_24
import mythscape.app.shared.generated.resources.ic_close_24
import mythscape.app.shared.generated.resources.ic_delete_24
import mythscape.app.shared.generated.resources.ic_edit_24
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.text.compareTo
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

    Page {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PageHeader(
                "Campaign Sessions",
            ) {
                AppButton(
                    text = "Add",
                    modifier = Modifier
                        .width(120.dp),
                    onClick = { viewModel.openCreateDialog() },
                    icon = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_add_24),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                )
            }

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

                    SessionRow(
                        session = session,
                        today = today,
                        onCancelToggle = {
                            viewModel.toggleCanceledState(session)
                        },
                        onEdit = {
                            viewModel.openEditDialog(session)
                        },
                        onDelete = {
                            viewModel.deleteSession(session)
                        },
                    )
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

@Composable
private fun SessionRow(
    session: SessionDTO,
    today: LocalDateTime,
    onCancelToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val isPast = session.date < today.date
    val backgroundTint = if (session.canceled) {
        MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
    } else if (isPast) {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundTint, shape = MaterialTheme.shapes.medium)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = if (isPast) 0.3f else 1f),
                MaterialTheme.shapes.medium
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
                    InfoBox(
                        MaterialTheme.colorScheme.error,
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
        ) {
            if (!isPast) {
                AppButton(
                    text = if (session.canceled) "Re-schedule" else "Cancel",
                    modifier = Modifier.width(100.dp),
                    onClick = { onCancelToggle() },
                    icon = {
                        Icon(
                            painterResource(Res.drawable.ic_close_24),
                            contentDescription = null,
                            modifier = Modifier.size(SmallIconSize),
                        )
                    },
                    color = ColorVariant.Secondary,
                )
            }

            AppButton(
                text = "Edit",
                modifier = Modifier.width(80.dp),
                onClick = { onEdit() },
                icon = {
                    Icon(
                        painterResource(Res.drawable.ic_edit_24),
                        contentDescription = null,
                        modifier = Modifier.size(SmallIconSize),
                    )
                },
            )

            AppButton(
                text = "Delete",
                modifier = Modifier.width(80.dp),
                onClick = { onDelete() },
                icon = {
                    Icon(
                        painterResource(Res.drawable.ic_delete_24),
                        contentDescription = null,
                        modifier = Modifier.size(SmallIconSize),
                    )
                },
                color = ColorVariant.Error,
            )
        }
    }
}