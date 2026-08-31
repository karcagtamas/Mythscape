package eu.karcags.mythscape.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.common.SessionManager
import eu.karcags.mythscape.ui.components.common.AppButton
import eu.karcags.mythscape.viewmodel.AppViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreen(sessionManager: SessionManager = koinInject(), appViewModel: AppViewModel = koinViewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .width(300.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Dashboard",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Welcome back, Master Chronicler: ${sessionManager.getUsername() ?: "Anonymous"}",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                )
            }

            AppButton(
                text = "Logout",
                onClick = {
                    appViewModel.logout()
                }
            )
        }
    }
}