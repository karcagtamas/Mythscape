package eu.karcags.mythscape.ui.components.dialogs.campaign

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.dtos.sessions.SessionDTO
import eu.karcags.mythscape.dtos.sessions.SessionRequestDTO
import eu.karcags.mythscape.ui.components.common.AppButton
import eu.karcags.mythscape.ui.components.common.AppTextField
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Composable
fun SessionFormDialog(
    campaignId: Int,
    onDismiss: () -> Unit,
    onConfirm: (SessionRequestDTO) -> Unit,
    modifier: Modifier = Modifier,
    session: SessionDTO? = null,
    isProcessing: Boolean = false,
    errorMessage: String? = null,
) {
    val isEdit = session != null

    var dateStr by remember { mutableStateOf("") }
    var startStr by remember { mutableStateOf("") }
    var endStr by remember { mutableStateOf("") }
    var isCanceled by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(session) {
        if (isEdit) {
            dateStr = session.date.toString()
            startStr = session.startTime.toString()
            endStr = session.endTime.toString()
            isCanceled = session.canceled
        } else {
            dateStr = ""
            startStr = "18:00"
            endStr = "21:00"
            isCanceled = false
        }
        validationError = null
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .width(360.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium),
        title = {
            Text(
                text = if (isEdit) "Edit session" else "Create session",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AppTextField(
                    value = dateStr,
                    onValueChange = { dateStr = it; validationError = null },
                    label = "Date (YYYY-MM-DD)"
                )

                AppTextField(
                    value = startStr,
                    onValueChange = { startStr = it; validationError = null },
                    label = "Start Time (HH:MM)"
                )

                AppTextField(
                    value = endStr,
                    onValueChange = { endStr = it; validationError = null },
                    label = "End Time (HH:MM)"
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .clickable { isCanceled = !isCanceled },
                ) {
                    Checkbox(
                        checked = isCanceled,
                        onCheckedChange = { isCanceled = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.error,
                            uncheckedColor = MaterialTheme.colorScheme.outline,
                        ),
                        modifier = Modifier.size(20.dp),
                    )

                    Text(
                        text = "Mark session as canceled",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }

                val activeError = validationError ?: errorMessage

                activeError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 2.dp, top = 2.dp),
                    )
                }
            }
        },
        confirmButton = {
            Box(modifier = Modifier.width(84.dp)) {
                AppButton(
                    text = "Save",
                    isLoading = isProcessing,
                    enabled = dateStr.isNotBlank() && startStr.isNotBlank() && endStr.isNotBlank(),
                    onClick = {
                        try {
                            val parsedDate = LocalDate.parse(dateStr)
                            val parsedStartTime = LocalTime.parse(startStr)
                            val parsedEndTime = LocalTime.parse(endStr)

                            onConfirm(
                                SessionRequestDTO(
                                    date = parsedDate,
                                    startTime = parsedStartTime,
                                    endTime = parsedEndTime,
                                    canceled = isCanceled,
                                    campaignId = campaignId,
                                )
                            )
                        } catch (e: Exception) {
                            validationError = "Invalid Date or Time syntax"
                        }
                    }
                )
            }
        },
        dismissButton = {
            Box(modifier = Modifier.width(84.dp)) {
                AppButton(
                    text = "Cancel",
                    onClick = { onDismiss() },
                )
            }
        },
    )
}