package eu.karcags.mythscape

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import eu.karcags.mythscape.ui.auth.AuthScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        AuthScreen(onAuthSuccess = { token ->
            println("Authenticated! Shared token: $token")
        })
    }
}