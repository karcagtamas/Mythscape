package eu.karcags.mythscape.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import eu.karcags.mythscape.enums.FormState
import eu.karcags.mythscape.enums.WorkspaceState
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
            activeView = workspaceViewModel.activeView,
            campaigns = campaigns,
            selectedCampaign = workspaceViewModel.selectedCampaign,
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
                activeView = workspaceViewModel.activeView,
                selectedCampaign = workspaceViewModel.selectedCampaign,
                onAppTitleClick = { workspaceViewModel.selectDashboard() },
                onVersionClick = {}
            )

            HorizontalLine()

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                when (workspaceViewModel.activeView) {
                    WorkspaceState.DASHBOARD -> {
                        DashboardScreen()
                    }

                    WorkspaceState.PROFILE -> {
                        ProfileScreen()
                    }

                    WorkspaceState.CAMPAIGN_DASHBOARD -> {
                        workspaceViewModel.selectedCampaign?.let {
                            CampaignDashboardScreen(
                                it.id,
                                onEdit = { campaign ->
                                    workspaceViewModel.selectCampaignEdit(campaign)
                                }
                            )
                        }
                    }

                    WorkspaceState.CAMPAIGN_CREATE -> {
                        CampaignFormScreen(
                            mode = FormState.CREATE,
                            onCancel = {
                                workspaceViewModel.selectDashboard()
                            },
                            onComplete = {
                                workspaceViewModel.loadUserCampaigns()
                                // TODO
                                // workspaceViewModel.selectCampaign()
                            }
                        )
                    }

                    WorkspaceState.CAMPAIGN_EDIT -> {
                        CampaignFormScreen(
                            mode = FormState.EDIT,
                            campaign = workspaceViewModel.selectedCampaign,
                            onCancel = {
                                workspaceViewModel.selectCampaign(workspaceViewModel.selectedCampaign!!)
                            },
                            onComplete = {
                                workspaceViewModel.loadUserCampaigns()
                                // TODO
                                // workspaceViewModel.selectCampaign()
                            }
                        )
                    }
                }
            }
        }
    }
}