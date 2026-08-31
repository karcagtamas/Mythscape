package eu.karcags.mythscape.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
                    ScreenFocus.DASHBOARD -> {
                        DashboardScreen()
                    }

                    ScreenFocus.PROFILE -> {
                        ProfileScreen()
                    }

                    ScreenFocus.CAMPAIGN_DASHBOARD -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                "CAMPAIGN MANAGER SCREEN FOR: ${workspaceViewModel.selectedCampaign?.name}",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}