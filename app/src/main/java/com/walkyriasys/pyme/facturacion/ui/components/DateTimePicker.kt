package com.walkyriasys.pyme.facturacion.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * A reusable Material Design 3 datetime picker component for Jetpack Compose.
 *
 * @param modifier Modifier for the composable.
 * @param initialDateTime Initial datetime to display (default: current time).
 * @param formatter Pattern for formatting the datetime output (default: "yyyy-MM-dd HH:mm").
 * @param onDateTimeSelected Callback invoked with the selected LocalDateTime when confirmed.
 * @param label Optional label for the text field (e.g., "Select Date and Time").
 * @param content Custom trigger composable (default: OutlinedTextField with formatted datetime).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatetimePicker(
    modifier: Modifier = Modifier,
    initialDateTime: LocalDateTime = LocalDateTime.now(),
    formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
    onDateTimeSelected: (LocalDateTime) -> Unit,
    label: String? = null,
    content: @Composable ((String, () -> Unit) -> Unit)? = null
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedDateTime by remember { mutableStateOf(initialDateTime) }
    var tempDateMillis by remember { mutableStateOf<Long?>(null) }

    // Default content: OutlinedTextField
    val defaultContent: @Composable (String, () -> Unit) -> Unit = { formattedDateTime, onClick ->
        OutlinedTextField(
            value = formattedDateTime,
            onValueChange = {},
            label = { label?.let { Text(it) } ?: Text("Date and Time") },
            readOnly = true,
            modifier = modifier,
            trailingIcon = {
                IconButton(onClick = onClick) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Select Date and Time"
                    )
                }
            }
        )
    }

    // Render trigger content
    (content ?: defaultContent).invoke(
        selectedDateTime.format(formatter),
        { showDatePicker = true }
    )

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        tempDateMillis = datePickerState.selectedDateMillis
                        showDatePicker = false
                        showTimePicker = true
                    }
                ) {
                    Text("Next")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Time Picker Dialog
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedDateTime.hour,
            initialMinute = selectedDateTime.minute,
            is24Hour = false // Customize as needed
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Combine date and time
                        val date = tempDateMillis?.let {
                            Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        } ?: selectedDateTime.toLocalDate()
                        val time = LocalDateTime.of(
                            date,
                            java.time.LocalTime.of(timePickerState.hour, timePickerState.minute)
                        )
                        selectedDateTime = time
                        onDateTimeSelected(time)
                        showTimePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Select Time") },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}