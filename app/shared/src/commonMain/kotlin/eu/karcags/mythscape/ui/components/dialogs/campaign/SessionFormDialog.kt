package eu.karcags.mythscape.ui.components.dialogs.campaign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.karcags.mythscape.dtos.sessions.SessionDTO
import eu.karcags.mythscape.dtos.sessions.SessionRequestDTO
import eu.karcags.mythscape.ui.components.common.fields.AppTextField
import eu.karcags.mythscape.ui.components.common.fields.CheckboxField
import eu.karcags.mythscape.ui.components.dialogs.EditDialog
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

    EditDialog(
        onDismiss = onDismiss,
        onOk = {
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
            } catch (_: Exception) {
                validationError = "Invalid Date or Time syntax"
            }
        },
        title = if (isEdit) "Edit Session" else "Schedule Session",
        modifier = modifier
            .width(400.dp),
        okText = if (isEdit) "Edit" else "Schedule",
        isProcessing = isProcessing,
        okEnabled = dateStr.isNotBlank() && startStr.isNotBlank() && endStr.isNotBlank(),
        errorMessage = validationError ?: errorMessage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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

            CheckboxField(
                isChecked = isCanceled,
                onCheckedChange = { isCanceled = it },
                checkedColor = MaterialTheme.colorScheme.error,
                caption = "Mark session as canceled",
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}