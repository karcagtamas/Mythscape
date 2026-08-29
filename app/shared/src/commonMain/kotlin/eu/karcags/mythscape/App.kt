package eu.karcags.mythscape

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import eu.karcags.mythscape.di.appModule
import eu.karcags.mythscape.theme.AppTheme
import eu.karcags.mythscape.ui.auth.AuthScreen
import org.koin.compose.KoinApplication
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
            AuthScreen(onAuthSuccess = { token ->
                println("Type-safe authentication payload parsed: $token")
            })
        }
    })
}