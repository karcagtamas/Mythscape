package eu.karcags.mythscape.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.ui.components.AppButton
import eu.karcags.mythscape.ui.components.AppTextField

enum class AuthMode {
    LOGIN,
    REGISTER,
}

@Composable
fun AuthScreen(
    onAuthSuccess: (token: String) -> Unit
) {
    var mode by remember { mutableStateOf(AuthMode.LOGIN) }

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }

    var errors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var isProcessing by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF0B0C10),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .width(400.dp)
                .background(Color(0xFF1F2833), shape = RoundedCornerShape(12.dp))
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = if (mode == AuthMode.LOGIN) "LOGIN" else "REGISTER",
                color = Color(0xFF6C0202),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            AppTextField(
                value = username,
                onValueChange = { username = it; errors = errors - "username" },
                label = "Username",
                errorMessage = errors["username"],
            )

            if (mode == AuthMode.REGISTER) {
                AppTextField(
                    value = email,
                    onValueChange = { email = it; errors = errors - "email" },
                    label = "E-mail Address",
                    errorMessage = errors["email"]
                )
            }

            AppTextField(
                value = password,
                onValueChange = { password = it; errors = errors - "password" },
                label = "Password",
                isPassword = true,
                errorMessage = errors["password"]
            )

            if (mode == AuthMode.REGISTER) {
                AppTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; errors = errors - "confirmPassword" },
                    label = "Confirm Password",
                    isPassword = true,
                    errorMessage = errors["confirmPassword"]
                )

                AppTextField(
                    value = fullName,
                    onValueChange = { fullName = it; errors = errors - "fullName" },
                    label = "Full Name",
                    errorMessage = errors["fullName"]
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            AppButton(
                text = if (mode == AuthMode.LOGIN) "Login" else "Register",
                isLoading = isProcessing,
                onClick = {
                    val localErrors = mutableMapOf<String, String>()

                    if (username.length < 6) localErrors["username"] = "Username must be at least 6 characters"
                    if (password.length < 8) localErrors["password"] = "Password must be at least 8 characters"

                    if (mode == AuthMode.REGISTER) {
                        if (!email.contains("@")) localErrors["email"] = "Invalid e-mail format string"
                        if (password != confirmPassword) localErrors["confirmPassword"] = "Passwords do not match"
                        if (fullName.isBlank()) localErrors["fullName"] = "Full name required"
                    }

                    if (localErrors.isNotEmpty()) {
                        errors = localErrors
                    } else {
                        isProcessing = true
                    }
                }
            )

            Text(
                text = if (mode == AuthMode.LOGIN)
                    "Need a mapmaker profile? Create an account"
                else "Already registered? Go back to login",
                color = Color(0xFF3B82F6),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        mode = if (mode == AuthMode.LOGIN) AuthMode.REGISTER else AuthMode.LOGIN
                        errors = emptyMap()
                        password = ""
                        confirmPassword = ""
                    }
            )
        }
    }
}