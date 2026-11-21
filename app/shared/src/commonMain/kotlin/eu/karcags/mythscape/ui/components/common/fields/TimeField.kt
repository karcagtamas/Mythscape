package eu.karcags.mythscape.ui.components.common.fields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.utils.padded
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import mythscape.app.shared.generated.resources.Res
import mythscape.app.shared.generated.resources.ic_date_range_24
import mythscape.app.shared.generated.resources.ic_schedule_24
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeField(
    time: LocalTime?,
    onTimeChange: (LocalTime?) -> Unit,
    label: String,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
) {
    var showPicker by remember { mutableStateOf(false) }
    val formattedTime = remember(time) {
        time?.let {
            "${it.hour.padded()}:${it.minute.padded()}"
        } ?: ""
    }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = formattedTime,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = {
                Icon(painterResource(Res.drawable.ic_schedule_24), contentDescription = "Select time")
            },
            isError = errorMessage != null,
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                errorTextColor = MaterialTheme.colorScheme.error,

                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                errorContainerColor = MaterialTheme.colorScheme.background,

                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error,

                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.outline,
                errorLabelColor = MaterialTheme.colorScheme.error,

                focusedLeadingIconColor = MaterialTheme.colorScheme.outline,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline,
                focusedTrailingIconColor = MaterialTheme.colorScheme.outline,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.outline,
            ),
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 2.dp, top = 1.dp),
            )
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { showPicker = true }
        )
    }

    if (showPicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = time?.hour ?: 9,
            initialMinute = time?.minute ?: 0,
            is24Hour = true
        )
        var displayMode by remember { mutableStateOf(TimePickerDisplayMode.Picker) }

        TimePickerDialog(
            onDismissRequest = { showPicker = false },
            title = { TimePickerDialogDefaults.Title(displayMode = displayMode) },
            modeToggleButton = {
                TimePickerDialogDefaults.DisplayModeToggle(
                    onDisplayModeChange = {
                        displayMode = if (displayMode == TimePickerDisplayMode.Picker) {
                            TimePickerDisplayMode.Input
                        } else {
                            TimePickerDisplayMode.Picker
                        }
                    },
                    displayMode = displayMode
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onTimeChange(LocalTime(timePickerState.hour, timePickerState.minute))
                        showPicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("Cancel") }
            }
        ) {
            if (displayMode == TimePickerDisplayMode.Picker) {
                TimePicker(state = timePickerState)
            } else {
                TimeInput(state = timePickerState)
            }
        }
    }
}