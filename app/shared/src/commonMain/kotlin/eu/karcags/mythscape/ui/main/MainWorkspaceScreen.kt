package eu.karcags.mythscape.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import eu.karcags.mythscape.enums.FormState
import eu.karcags.mythscape.enums.WorkspaceScreenState
import eu.karcags.mythscape.ui.components.common.HorizontalLine
import eu.karcags.mythscape.ui.components.common.VerticalLine
import eu.karcags.mythscape.ui.components.dialogs.campaign.CampaignFormDialog
import eu.karcags.mythscape.ui.components.main.MainBar
import eu.karcags.mythscape.ui.components.main.WorkspaceBar
import eu.karcags.mythscape.ui.dashboard.DashboardScreen
import eu.karcags.mythscape.ui.main.campaign.CampaignWorkspaceScreen
import eu.karcags.mythscape.viewmodel.AppViewModel
import eu.karcags.mythscape.viewmodel.WorkspaceViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainWorkspaceScreen(
    appViewModel: AppViewModel = koinViewModel(),
    workspaceViewModel: WorkspaceViewModel = koinViewModel(),
) {
    val campaigns by workspaceViewModel.campaigns.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {

        WorkspaceBar(
            state = workspaceViewModel.state,
            campaigns = campaigns,
            onProfileSelect = { workspaceViewModel.selectProfile() },
            onCampaignSelect = { workspaceViewModel.selectCampaign(it) },
            onCampaignCreate = { workspaceViewModel.openCampaignCreateDialog() },
            onLogout = { appViewModel.logout() },
        )

        VerticalLine()

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        ) {
            MainBar(
                state = workspaceViewModel.state,
                onAppTitleClick = { workspaceViewModel.selectDashboard() },
                onVersionClick = {}
            )

            HorizontalLine()

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                when (workspaceViewModel.state) {
                    WorkspaceScreenState.Dashboard -> {
                        DashboardScreen()
                    }

                    WorkspaceScreenState.Profile -> {
                        ProfileScreen()
                    }

                    is WorkspaceScreenState.CampaignWorkspace -> {
                        CampaignWorkspaceScreen(
                            campaignId = (workspaceViewModel.state as WorkspaceScreenState.CampaignWorkspace).campaignId,
                            onEdit = {
                                workspaceViewModel.loadUserCampaigns()
                                workspaceViewModel.updateCampaign(it.title)
                            },
                            onDelete = {
                                workspaceViewModel.loadUserCampaigns()
                                workspaceViewModel.selectDashboard()
                            },
                            onArchive = {
                                workspaceViewModel.loadUserCampaigns()
                                workspaceViewModel.selectDashboard()
                            },
                        )
                    }
                }
            }
        }
    }

    if (workspaceViewModel.showCampaignCreateDialog) {
        CampaignFormDialog(
            mode = FormState.CREATE,
            isProcessing = workspaceViewModel.isCreatingCampaign,
            errorMessage = workspaceViewModel.createCampaignError,
            onDismiss = { workspaceViewModel.closeCampaignCreateDialog() },
            onConfirm = {
                workspaceViewModel.createCampaign(it)
                if (workspaceViewModel.createCampaignError == null) {
                    workspaceViewModel.closeCampaignCreateDialog()
                }
            }
        )
    }
}