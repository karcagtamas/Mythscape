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
import kotlinx.datetime.parseOrNull

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

    var date by remember { mutableStateOf<LocalDate?>(null) }
    var start by remember { mutableStateOf<LocalTime?>(LocalTime(18, 0)) }
    var end by remember { mutableStateOf<LocalTime?>(LocalTime(21, 0)) }
    var isCanceled by remember { mutableStateOf(false) }

    LaunchedEffect(session) {
        if (isEdit) {
            date = session.date
            start = session.startTime
            end = session.endTime
            isCanceled = session.canceled
        } else {
            date = null
            start = LocalTime(18, 0)
            end = LocalTime(21, 0)
            isCanceled = false
        }
    }

    val dateError by remember {
        derivedStateOf {
            when {
                date == null -> "Date is required"
                else -> null
            }
        }
    }

    val startError by remember {
        derivedStateOf {
            when {
                start == null -> "Start Time is required"
                else -> null
            }
        }
    }

    val endError by remember {
        derivedStateOf {
            when {
                end == null -> "End Time is required"
                else -> null
            }
        }
    }

    EditDialog(
        onDismiss = onDismiss,
        onOk = {
            onConfirm(
                SessionRequestDTO(
                    date = date!!,
                    startTime = start!!,
                    endTime = end!!,
                    canceled = isCanceled,
                    campaignId = campaignId,
                )
            )
        },
        title = if (isEdit) "Edit Session" else "Schedule Session",
        modifier = modifier
            .width(400.dp),
        okText = if (isEdit) "Edit" else "Schedule",
        isProcessing = isProcessing,
        okEnabled = dateError == null && startError == null && endError == null,
        errorMessage = errorMessage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AppTextField(
                value = date.toString(),
                onValueChange = { date = LocalDate.parseOrNull(it) },
                label = "Date (YYYY-MM-DD)",
                errorMessage = dateError,
            )

            AppTextField(
                value = start.toString(),
                onValueChange = { start = LocalTime.parseOrNull(it) },
                label = "Start Time (HH:MM)",
                errorMessage = startError,
            )

            AppTextField(
                value = end.toString(),
                onValueChange = { end = LocalTime.parseOrNull(it) },
                label = "End Time (HH:MM)",
                errorMessage = endError,
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