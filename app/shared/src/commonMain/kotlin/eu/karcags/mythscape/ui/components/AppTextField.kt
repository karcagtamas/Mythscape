package eu.karcags.mythscape.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mythscape.app.shared.generated.resources.Res
import mythscape.app.shared.generated.resources.visibility_24
import mythscape.app.shared.generated.resources.visibility_off_24
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector? = null,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    errorMessage: String? = null,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, color = Color.Gray) },
            leadingIcon = leadingIcon?.let { icon ->
                { Icon(icon, contentDescription = null, tint = Color.Gray) }
            },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painterResource(if (passwordVisible) Res.drawable.visibility_24 else Res.drawable.visibility_off_24),
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            isError = errorMessage != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFF6C0202),
                unfocusedBorderColor = Color(0xFF455161),
                errorBorderColor = Color(0xFF9F0000),
                focusedContainerColor = Color(0xFF0B0C10),
                unfocusedContainerColor = Color(0xFF0B0C10)
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        if (errorMessage != null) {
            Text(
                errorMessage,
                color = Color.Red,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }
    }
}