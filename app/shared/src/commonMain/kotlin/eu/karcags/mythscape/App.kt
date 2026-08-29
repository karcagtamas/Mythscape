package eu.karcags.mythscape

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.karcags.mythscape.common.appModule
import eu.karcags.mythscape.theme.AppTheme
import eu.karcags.mythscape.ui.auth.AuthScreen
import eu.karcags.mythscape.ui.dashboard.DashboardScreen
import eu.karcags.mythscape.viewmodel.AppState
import eu.karcags.mythscape.viewmodel.AppViewModel
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.koinConfiguration

@Composable
@Preview
fun App() {
    KoinApplication(configuration = koinConfiguration(declaration = {
        modules(
            appModule,
        )
    }), content = {
        AppTheme {
            val appViewModel: AppViewModel = koinViewModel()
            val navigationState by appViewModel.state.collectAsState()

            when (navigationState) {
                AppState.INITIALIZING -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                AppState.UNAUTHENTICATED -> {
                    AuthScreen(onAuthSuccess = {
                        appViewModel.setAuthenticated()
                    })
                }

                AppState.AUTHENTICATED -> {
                    DashboardScreen()
                }
            }
        }
    })
}