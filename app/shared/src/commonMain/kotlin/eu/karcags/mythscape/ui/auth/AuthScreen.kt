package eu.karcags.mythscape.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.ui.components.common.AppButton
import eu.karcags.mythscape.ui.components.common.AppTextField
import eu.karcags.mythscape.viewmodel.AuthMode
import eu.karcags.mythscape.viewmodel.AuthViewModel
import mythscape.app.shared.generated.resources.Res
import mythscape.app.shared.generated.resources.email_24
import mythscape.app.shared.generated.resources.main
import mythscape.app.shared.generated.resources.password_24
import mythscape.app.shared.generated.resources.person_24
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun AuthScreen(
    onAuthSuccess: (token: String) -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
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
                text = if (viewModel.mode == AuthMode.LOGIN) "LOGIN" else "REGISTER",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(
                modifier = Modifier
                    .height(4.dp),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(Res.drawable.main),
                    contentDescription = "Mythscape logo",
                    modifier = Modifier.size(120.dp),
                )
            }

            Spacer(
                modifier = Modifier
                    .height(4.dp),
            )

            AppTextField(
                value = viewModel.username,
                onValueChange = { viewModel.username = it },
                label = "Username",
                leadingIcon = painterResource(Res.drawable.person_24),
                errorMessage = errors["username"],
            )

            if (viewModel.mode == AuthMode.REGISTER) {
                AppTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = "E-mail Address",
                    leadingIcon = painterResource(Res.drawable.email_24),
                    errorMessage = errors["email"]
                )
            }

            AppTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = "Password",
                leadingIcon = painterResource(Res.drawable.password_24),
                isPassword = true,
                errorMessage = errors["password"]
            )

            if (viewModel.mode == AuthMode.REGISTER) {
                AppTextField(
                    value = viewModel.passwordConfirm,
                    onValueChange = { viewModel.passwordConfirm = it },
                    label = "Confirm Password",
                    leadingIcon = painterResource(Res.drawable.password_24),
                    isPassword = true,
                    errorMessage = errors["passwordConfirm"]
                )

                AppTextField(
                    value = viewModel.fullname,
                    onValueChange = { viewModel.fullname = it },
                    label = "Full Name",
                    errorMessage = errors["fullName"]
                )
            }

            errors["global"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(2.dp))

            AppButton(
                text = if (viewModel.mode == AuthMode.LOGIN) "Login" else "Register",
                isLoading = isProcessing,
                onClick = {
                    viewModel.submit(onAuthSuccess)
                }
            )

            Text(
                text = if (viewModel.mode == AuthMode.LOGIN) "Create an account" else "Back to login",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().clickable {
                    viewModel.toggleAuthMode()
                }
            )
        }
    }
}