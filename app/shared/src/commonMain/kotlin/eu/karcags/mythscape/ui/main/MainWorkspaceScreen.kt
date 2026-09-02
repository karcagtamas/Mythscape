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
import eu.karcags.mythscape.ui.components.main.MainBar
import eu.karcags.mythscape.ui.components.main.WorkspaceBar
import eu.karcags.mythscape.ui.dashboard.DashboardScreen
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
            onCampaignCreate = { workspaceViewModel.selectCampaignCreate() },
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

                    is WorkspaceScreenState.CampaignDashboard -> {
                        CampaignDashboardScreen(
                            (workspaceViewModel.state as WorkspaceScreenState.CampaignDashboard).campaign.id,
                            onEdit = { campaign ->
                                workspaceViewModel.selectCampaignEdit(campaign)
                            }
                        )
                    }

                    WorkspaceScreenState.CampaignCreate -> {
                        CampaignFormScreen(
                            mode = FormState.CREATE,
                            onCancel = {
                                workspaceViewModel.selectDashboard()
                            },
                            onComplete = { campaign ->
                                workspaceViewModel.loadUserCampaigns()
                                workspaceViewModel.selectCampaign(campaign)
                            }
                        )
                    }

                    is WorkspaceScreenState.CampaignEdit -> {
                        val campaign = (workspaceViewModel.state as WorkspaceScreenState.CampaignEdit).campaign
                        CampaignFormScreen(
                            mode = FormState.EDIT,
                            campaign = campaign,
                            onCancel = {
                                workspaceViewModel.selectCampaign(campaign)
                            },
                            onComplete = { campaign ->
                                workspaceViewModel.loadUserCampaigns()
                                workspaceViewModel.selectCampaign(campaign)
                            }
                        )
                    }
                }
            }
        }
    }
}