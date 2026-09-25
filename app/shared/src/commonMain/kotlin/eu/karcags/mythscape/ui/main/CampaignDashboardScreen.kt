package eu.karcags.mythscape.ui.main

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
import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignMemberDTO
import eu.karcags.mythscape.dtos.sessions.SessionDTO
import eu.karcags.mythscape.ui.components.common.*
import eu.karcags.mythscape.ui.components.dialogs.ConfirmDialog
import eu.karcags.mythscape.utils.formatted
import eu.karcags.mythscape.viewmodel.CampaignDashboardViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CampaignDashboardScreen(
    campaignId: Int,
    onEdit: (CampaignDTO) -> Unit,
    onDelete: (CampaignDTO) -> Unit,
    onArchive: (CampaignDTO) -> Unit,
    viewModel: CampaignDashboardViewModel = koinViewModel(),
) {
    LaunchedEffect(campaignId) {
        viewModel.initialize(campaignId)
    }

    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showArchiveConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(campaignId) {
        showDeleteConfirm = false
        showArchiveConfirm = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(18.dp),
        contentAlignment = Alignment.TopStart,
    ) {
        LoadingBox(
            isLoading = viewModel.isLoading,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    viewModel.campaign?.let { campaign ->
                        CampaignSummary(
                            campaign = campaign,
                            onEdit = onEdit,
                            onConfirmDelete = {
                                viewModel.delete(campaign.id) {
                                    onDelete(it)
                                }
                            },
                            onConfirmArchive = {
                                viewModel.archive(campaign.id) {
                                    onArchive(it)
                                }
                            },
                            isProcessing = viewModel.isProcessing,
                        )
                    }

                    CampaignRecentSessions(
                        recentSessions = viewModel.recentSessions,
                    )
                }

                CampaignMembers(
                    members = viewModel.members,
                )
            }
        }
    }
}

@Composable
private fun CampaignSummary(
    campaign: CampaignDTO,
    onEdit: (CampaignDTO) -> Unit,
    onConfirmDelete: (CampaignDTO) -> Unit,
    onConfirmArchive: (CampaignDTO) -> Unit,
    isProcessing: Boolean = false,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showArchiveDialog by remember { mutableStateOf(false) }

    PrimaryCard(
        title = campaign.title,
    ) {
        if (campaign.description != null && campaign.description!!.isNotEmpty()) {
            Text(
                text = if (campaign.description.isNullOrBlank()) "No description provided" else campaign.description.orEmpty(),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp,
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        MetaRow(label = "Created By", value = campaign.creator.name)
        MetaRow(label = "Launch", value = campaign.creation.formatted())
        MetaRow(label = "Last Update", value = campaign.lastUpdate.formatted())

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier.weight(1f),
            ) {
                AppButton(
                    text = "Edit",
                    onClick = { onEdit(campaign) },
                )
            }

            Box(
                modifier = Modifier.weight(1f),
            ) {
                AppButton(
                    text = "Delete",
                    onClick = { showDeleteDialog = true },
                    color = MaterialTheme.colorScheme.error,
                    textColor = MaterialTheme.colorScheme.onError,
                )
            }

            Box(
                modifier = Modifier.weight(1f),
            ) {
                AppButton(
                    text = "Archive",
                    onClick = { showArchiveDialog = true },
                    color = MaterialTheme.colorScheme.error,
                    textColor = MaterialTheme.colorScheme.onError,
                )
            }
        }
    }

    if (showDeleteDialog) {
        ConfirmDialog(
            title = "Delete Campaign",
            message = "Do you want to delete this campaign?",
            confirmText = "Delete",
            onDismiss = { showDeleteDialog = false },
            onConfirm = { onConfirmDelete(campaign) },
            isProcessing = isProcessing,
        )
    }

    if (showArchiveDialog) {
        ConfirmDialog(
            title = "Archive Campaign",
            message = "Do you want to archive this campaign?",
            confirmText = "Archive",
            onDismiss = { showArchiveDialog = false },
            onConfirm = { onConfirmArchive(campaign) },
            isProcessing = isProcessing,
        )
    }
}

@Composable
private fun ColumnScope.CampaignRecentSessions(
    recentSessions: List<SessionDTO>,
) {
    SecondaryCard(
        title = "Recent Sessions",
        modifier = Modifier
            .weight(1f),
    ) {
        if (recentSessions.isEmpty()) {
            Text(
                "No recent sessions available",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp
            )
        } else {
            recentSessions.forEach { session ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Session ${session.id}",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = "${session.date} [${session.startTime.hour}:${
                            session.startTime.minute.toString().padStart(2, '0')
                        } - ${session.endTime.hour}:${
                            session.endTime.minute.toString().padStart(2, '0')
                        }]",
                        color = Color.Gray,
                        fontSize = 11.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.CampaignMembers(
    members: List<CampaignMemberDTO>,
) {
    SecondaryCard(
        title = "Members",
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(members) { member ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.background.copy(alpha = 0.3f),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(vertical = 6.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = member.name,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = "Joined: ${member.creation.formatted()}",
                            color = Color.DarkGray,
                            fontSize = 10.sp,
                        )
                    }

                    if (member.isDM) {
                        Text(
                            text = "DM",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                        )
                    } else {
                        Text(
                            text = "PLAYER",
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}