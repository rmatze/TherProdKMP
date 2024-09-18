package com.matze.therprodkmp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.matze.therprodkmp.model.TimesheetRequest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import therprodkmp.composeapp.generated.resources.Res
import therprodkmp.composeapp.generated.resources.cancel
import therprodkmp.composeapp.generated.resources.clock_in
import therprodkmp.composeapp.generated.resources.clock_out
import therprodkmp.composeapp.generated.resources.next
import therprodkmp.composeapp.generated.resources.select_date

//import java.util.Calendar

@Composable
fun WorkdayDateScreen(
    onSelectionChanged: (String) -> Unit = {},
    onCancelButtonClicked: () -> Unit = {},
    onNextButtonClicked: (String, List<TimesheetRequest>) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDateModal by remember { mutableStateOf(false) }
    var showClockInTimeModal by remember { mutableStateOf(false) }
    var showClockOutTimeModal by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<String>("") }
    var selectedClockInTime by remember { mutableStateOf<String?>("") }
    var selectedClockInTimeDisplay by remember { mutableStateOf<String?>("") }
    var selectedClockOutTime by remember { mutableStateOf<String?>("") }
    var selectedClockOutTimeDisplay by remember { mutableStateOf<String?>("") }
    var clockIn by remember { mutableStateOf(true) }

    if (showDateModal) {
        DatePickerModal(
            onDateSelected = {
                selectedDate = it!!
                showDateModal = false
            },
            onDismiss = { showDateModal = false }
        )
    }

    if (showClockInTimeModal) {
        TimePickerModal(
            onTimeSelected = { displayTime, dbDate ->
                selectedClockInTimeDisplay = displayTime
                selectedClockInTime = dbDate
                showClockInTimeModal = false
            },
            onDismiss = { showClockInTimeModal = false }
        )
    }

    if (showClockOutTimeModal) {
        TimePickerModal(
            onTimeSelected = { displayTime, dbDate ->
                selectedClockOutTimeDisplay = displayTime
                selectedClockOutTime = dbDate
                showClockOutTimeModal = false
            },
            onDismiss = { showClockOutTimeModal = false }
        )
    }

    var selectedValue by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                if (selectedDate.isNotBlank()) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = selectedDate
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = "Select Date"
                    )
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        showDateModal = true
                        clockIn = true
                    }
                ) {
                    Text(stringResource(Res.string.select_date))
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                if (!selectedClockInTimeDisplay.isNullOrBlank()) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = "$selectedClockInTimeDisplay"
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = "Select Time"
                    )
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        showClockInTimeModal = true
                        clockIn = true
                    }
                ) {
                    Text(stringResource(Res.string.clock_in))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                if (!selectedClockOutTimeDisplay.isNullOrBlank() && !clockIn) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = "$selectedClockOutTimeDisplay"
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = "Select Time"
                    )
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        showClockOutTimeModal = true
                        clockIn = false
                    }
                ) {
                    Text(stringResource(Res.string.clock_out))
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onCancelButtonClicked
            ) {
                Text(stringResource(Res.string.cancel))
            }
            Button(
                modifier = Modifier.weight(1f),
                // the button is enabled when the user makes a selection
                enabled = selectedClockInTime!!.isNotEmpty(),
                onClick = {
                    onNextButtonClicked(
                        selectedDate,
                        listOf(
                            TimesheetRequest(
                                clockIn = convertStringToInstant(
                                    selectedClockInTime ?: ""
                                ), //we know this will never be null since we don't enable the button until it's not empty or null
                                clockOut = convertStringToInstantNullable(selectedClockOutTime)
                            )
                        )
                    )
                }
            ) {
                Text(stringResource(Res.string.next))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                println("Date selected: ${datePickerState.selectedDateMillis}")
                onDateSelected(
                    if (datePickerState.selectedDateMillis != null) {
                        formatDate(
                            Instant.fromEpochMilliseconds(datePickerState.selectedDateMillis!!)
                                .toLocalDateTime(TimeZone.UTC)
                        )
                    } else {
                        ""
                    }
                )
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerModal(
    onTimeSelected: (String?, String?) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Clock.System.now()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.toLocalDateTime(TimeZone.currentSystemDefault()).hour,
        initialMinute = currentTime.toLocalDateTime(TimeZone.currentSystemDefault()).minute,
        is24Hour = false
    )

    TimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = {
            println("Time selected: ${timePickerState.hour} + ${timePickerState.minute}")
            onTimeSelected(
                convert24hTo12h(timePickerState),
                convertToUnixTimestamp(timePickerState)
            )
//            onTimeSelected(convert24hTo12h(timePickerState))
        }
    ) {
        TimePicker(
            state = timePickerState,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun convertToUnixTimestamp(timePickerState: TimePickerState): String {
    // Step 1: Get the current date (you could choose any date you want)
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    // Step 2: Create LocalTime from the selected hour and minute
    val selectedTime = LocalTime(timePickerState.hour, timePickerState.minute)

    // Step 3: Combine the date and time to form LocalDateTime
    val localDateTime = LocalDateTime(
        currentDate.year,
        currentDate.month,
        currentDate.dayOfMonth,
        selectedTime.hour,
        selectedTime.minute
    )

    // Step 4: Convert LocalDateTime to UTC timestamp (Instant)
    val instantInUTC = localDateTime.toInstant(TimeZone.UTC)

    // Get the timestamp in milliseconds (Unix Epoch Time)
    val utcTimestampMillis = instantInUTC.toEpochMilliseconds()

    println("Selected Time in UTC (timestamp): $utcTimestampMillis")

    return convertUtcTimestampToLocalFormatted(utcTimestampMillis)
}

fun convertUtcTimestampToLocalFormatted(utcTimestampMillis: Long): String {
    // Step 1: Convert UTC timestamp (milliseconds) to an Instant
    val utcInstant = Instant.fromEpochMilliseconds(utcTimestampMillis)

    // Step 2: Convert the Instant to LocalDateTime in the local time zone
    val localDateTime =
        utcInstant.toLocalDateTime(TimeZone.UTC).toInstant(TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())

    // Step 3: Manually format the LocalDateTime (e.g., "yyyy-MM-dd HH:mm:ss")
    val year = localDateTime.year
    val month = localDateTime.monthNumber
    val day = localDateTime.dayOfMonth
    val hour = localDateTime.hour
    val minute = localDateTime.minute
    val second = localDateTime.second

    // Return a formatted string
//    return "%04d-%02d-%02d %02d:%02d:%02d".format(year, month, day, hour, minute, second)
    val result = "${year.toString().padStart(4, '0')}-${month.toString().padStart(2, '0')}-${
        day.toString().padStart(2, '0')
    }T" +
            "${hour.toString().padStart(2, '0')}:${
                minute.toString().padStart(2, '0')
            }:${second.toString().padStart(2, '0')}Z"

    println("Selected Time Formatted in LocalTime: $result")
    return result
}

fun convertStringToInstant(dateString: String): Instant {
    return Instant.parse(dateString)
}

fun convertStringToInstantNullable(dateString: String?): Instant? {
    if (!dateString.isNullOrBlank()) {
        return Instant.parse(dateString)
    }
    return null
}

fun convertInstantToString(instant: Instant): String {
    return convertUtcTimestampToLocalFormatted(instant.toEpochMilliseconds())
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
fun convert24hTo12h(timePickerState: TimePickerState): String {
    return if (timePickerState.hour < 12) {
        "${timePickerState.hour}" + ":" + "${padZero(timePickerState.minute)}${timePickerState.minute}" + " am"
    } else {
        "${timePickerState.hour - 12}" + ":" + "${padZero(timePickerState.minute)}${timePickerState.minute}" + " pm"
    }


//    val localTime = LocalTime.fromMillisecondOfDay(timePickerState.)   ..of(timePickerState.hour, timePickerState.minute)
//    val pattern = if (timePickerState.is24hour) "HH:mm" else "hh:mm a"
//    val formattedTime = localTime.format(DateTimeFormatter.ofPattern(pattern))
}

fun padZero(min: Int) = if (min < 10) "0" else ""

//fun formattedTime(hour: Int, minute: Int): String {
//    val formatter = DateTimeFormatter.ofPattern("HH:mm")
//    val time = LocalTime.of(hour, minute).format(formatter)
//    return time
//}

@Composable
fun DynamicTextRows() {
    var textRows by remember { mutableStateOf(listOf("")) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (text in textRows) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = text,
                    onValueChange = { newText ->
                        textRows = textRows.mapIndexed { index, oldText ->
                            if (index == textRows.indexOf(text)) newText else oldText
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                Spacer(modifier = Modifier.width(8.dp))
                if (textRows.size > 1) {
                    Button(onClick = { textRows = textRows.filter { it != text } }) {
                        Text("Remove")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(onClick = { textRows = textRows + "" }) {
            Text("Add Row")
        }
    }
}

@Composable
fun DynamicButtonRows() {
    var buttonRows by remember { mutableStateOf(listOf<Pair<String, String>>()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for ((button1, button2) in buttonRows) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { /* Handle button 1 click */ }) {
                    Text(button1)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { /* Handle button 2 click */ }) {
                    Text(button2)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(onClick = { buttonRows = buttonRows + Pair("Button 1", "Button 2") }) {
            Text("Add Row")
        }
    }
}

@Composable
fun DynamicButtonRowsWithTextFields() {
    var buttonRows by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var textFields by remember { mutableStateOf(listOf("")) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        buttonRows.forEachIndexed { index, (button1, button2) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = textFields[index],
                            onValueChange = { newText ->
                                textFields = textFields.mapIndexed { i, oldText ->
                                    if (i == index) newText else oldText
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { /* Handle button 1 click */ }) {
                            Text(button1)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = textFields[index],
                            onValueChange = { newText ->
                                textFields = textFields.mapIndexed { i, oldText ->
                                    if (i == index) newText else oldText
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { /* Handle button 2 click */ }) {
                            Text(button2)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(onClick = {
            buttonRows = buttonRows + Pair("Button 1", "Button 2")
            textFields = textFields + ""
        }) {
            Text("Add Row")
        }
    }
}

@Composable
fun DynamicButtonRowsWithText() {
    var buttonRows by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    val texts = listOf("Text 1", "Text 2", "Text 3", "Text 4") // Example static text values

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        buttonRows.forEachIndexed { index, (button1, button2) ->
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = texts.getOrNull(index * 2) ?: "Default Text 1",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { /* Handle button 1 click */ }) {
                        Text(button1)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = texts.getOrNull(index * 2 + 1) ?: "Default Text 2",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { /* Handle button 2 click */ }) {
                        Text(button2)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(onClick = {
            buttonRows = buttonRows + Pair("Button 1", "Button 2")
        }) {
            Text("Add Row")
        }
    }
}

@Composable
fun DynamicButtonRowsWithSelectableText() {
    var buttonRows by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var selectedTexts by remember { mutableStateOf(mutableListOf<String>()) }
    var expandedIndices by remember { mutableStateOf(mutableSetOf<Int>()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        buttonRows.forEachIndexed { index, (button1, _) ->
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = selectedTexts.getOrNull(index) ?: "Select an option",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { expandedIndices.add(index) }) {
                        Text(button1)
                    }
                    DropdownMenu(
                        expanded = expandedIndices.contains(index),
                        onDismissRequest = { expandedIndices.remove(index) }
                    ) {
                        listOf("Option 1", "Option 2", "Option 3").forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedTexts[index] = option
                                    expandedIndices.remove(index)
                                })
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(onClick = {
            buttonRows = buttonRows + Pair("Button 1", "Button 2")
            selectedTexts.add("")
        }) {
            Text("Add Row")
        }
    }
}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TimePickerModal(
//    modifier: Modifier = Modifier
//) {
//    val timePickerState = rememberTimePickerState(
//        is24Hour = true
//    )
//    var timeSelected by remember { mutableStateOf("") }
//
//
//    var showDialog by remember { mutableStateOf(false) }
//    Column(
//        modifier = modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable(onClick = {
//                    showDialog = true
//                }),
//            text =  if (timeSelected.isEmpty()) "Select time" else timeSelected,
//            textAlign = TextAlign.Center,
//            style = MaterialTheme.typography.headlineMedium
//        )
//
//        if (showDialog) {
//            Dialog(
//                onDismissRequest = { showDialog = false },
//                properties = DialogProperties(usePlatformDefaultWidth = true)
//            ) {
//                ElevatedCard(
//                    modifier = Modifier
//                        .background(color = MaterialTheme.colorScheme.surface,
//                            shape = MaterialTheme.shapes.extraLarge),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
//                    shape = MaterialTheme.shapes.extraLarge
//                ) {
//                    Column(
//                        modifier = Modifier.padding(16.dp),
//                    ) {
//
//                        Text(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp),
//                            text = "Select time"
//                        )
//
//                        TimePicker(
//                            state = timePickerState,
//                            layoutType = TimePickerLayoutType.Vertical,
//                        )
//
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.End
//                        ) {
//
//                            Button(
//                                modifier = Modifier.padding(end = 8.dp),
//                                onClick = { showDialog = false }
//                            ) {
//                                Text(
//                                    text = "Cancel"
//                                )
//                            }
//
//                            Button(
//                                modifier = Modifier.padding(start = 8.dp),
//                                onClick = {
//                                    timeSelected = timeState.hour.toString() + timeState.minute.toString()//formattedTime(timeState.hour, timeState.minute)
//                                    showDialog = false
//                                }
//                            ) {
//                                Text(text = "OK")
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}


//fun convertMillisToDate(millis: Long): String {
//    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
//    return formatter.format(Date(millis))
//}

fun formatDate(time: LocalDateTime): String {
    val format = LocalDateTime.Format {

        monthNumber()
        char('-')
        dayOfMonth()
        char('-')
        year()

//        char(' ')
//
//        hour()
//        char(':')
//        minute()
//        char(':')
//        second()
    }

    // do some weird BS: UTC date-time (Long) -> UTC LocalDateTime (object) -> zone date-time (object) -> zone date-time (Long)
    // https://stackoverflow.com/questions/63929730/materialdatepicker-returning-wrong-value
    return format.format(
        time.toInstant(TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())
    )
}