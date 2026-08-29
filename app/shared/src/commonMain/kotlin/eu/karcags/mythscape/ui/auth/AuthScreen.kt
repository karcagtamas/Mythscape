package eu.karcags.mythscape.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.ui.components.AppButton
import eu.karcags.mythscape.ui.components.AppTextField
import eu.karcags.mythscape.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

enum class AuthMode {
    LOGIN,
    REGISTER,
}

@Composable
fun AuthScreen(
    onAuthSuccess: (token: String) -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    var mode by remember { mutableStateOf(AuthMode.LOGIN) }

    val isProcessing by viewModel.isProcessing.collectAsState()
    val errors by viewModel.errors.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .width(360.dp)
                .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = if (mode == AuthMode.LOGIN) "LOGIN" else "REGISTER",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            AppTextField(
                value = viewModel.username,
                onValueChange = { viewModel.username = it },
                label = "Username",
                errorMessage = errors["username"],
            )

            if (mode == AuthMode.REGISTER) {
                AppTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = "E-mail Address",
                    errorMessage = errors["email"]
                )
            }

            AppTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = "Password",
                isPassword = true,
                errorMessage = errors["password"]
            )

            if (mode == AuthMode.REGISTER) {
                AppTextField(
                    value = viewModel.passwordConfirm,
                    onValueChange = { viewModel.passwordConfirm = it },
                    label = "Confirm Password",
                    isPassword = true,
                    errorMessage = errors["passwordConfirm"]
                )

                AppTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.passwordConfirm = it },
                    label = "Full Name",
                    errorMessage = errors["fullName"]
                )
            }

            errors["global"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(2.dp))

            AppButton(
                text = if (mode == AuthMode.LOGIN) "Login" else "Register",
                isLoading = isProcessing,
                onClick = {
                    viewModel.submit(mode == AuthMode.LOGIN, onAuthSuccess)
                }
            )

            Text(
                text = if (mode == AuthMode.LOGIN) "Create an account" else "Back to login",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().clickable {
                    mode = if (mode == AuthMode.LOGIN) AuthMode.REGISTER else AuthMode.LOGIN
                }
            )
        }
    }
}