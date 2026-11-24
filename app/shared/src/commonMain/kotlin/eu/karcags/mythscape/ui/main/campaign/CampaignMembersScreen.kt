package eu.karcags.mythscape.ui.main.campaign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import eu.karcags.mythscape.dtos.campaigns.CampaignMemberDTO
import eu.karcags.mythscape.enums.ColorVariant
import eu.karcags.mythscape.theme.SmallIconSize
import eu.karcags.mythscape.ui.components.common.*
import eu.karcags.mythscape.ui.components.dialogs.campaign.CampaignMemberFormDialog
import eu.karcags.mythscape.ui.components.main.campaign.CampaignMemberRow
import eu.karcags.mythscape.utils.fromHex
import eu.karcags.mythscape.viewmodel.campaign.CampaignMembersViewModel
import mythscape.app.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CampaignMembersScreen(
    campaignId: Int,
    viewModel: CampaignMembersViewModel = koinViewModel(),
) {
    LaunchedEffect(campaignId) {
        viewModel.initialize(campaignId)
    }

    val members by viewModel.members.collectAsState()
    val users by viewModel.users.collectAsState()

    Page {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PageHeader(
                "Campaign Members",
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
                items(members) { member ->
                    MemberRow(
                        member = member,
                        onEdit = {
                            viewModel.openEditDialog(member)
                        },
                        onDelete = {
                            viewModel.delete(member)
                        },
                    )
                }
            }
        }

        if (viewModel.showFormDialog) {
            CampaignMemberFormDialog(
                member = viewModel.editing,
                users = users,
                isProcessing = viewModel.isProcessing,
                onDismiss = { viewModel.showFormDialog = false },
                onConfirm = { viewModel.save(it) },
            )
        }
    }
}

@Composable
private fun MemberRow(
    member: CampaignMemberDTO,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    CampaignMemberRow(
        member = member,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (!member.isDM) {
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
}