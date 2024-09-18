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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.matze.therprodkmp.model.MeetingRequest
import org.jetbrains.compose.resources.stringResource
import therprodkmp.composeapp.generated.resources.Res
import therprodkmp.composeapp.generated.resources.cancel
import therprodkmp.composeapp.generated.resources.next

@Composable
fun WorkdayMeetingScreen(
    onNextButtonClicked: (List<MeetingRequest>) -> Unit,
    onCancelButtonClicked: () -> Unit
) {

    var meetings by remember { mutableStateOf(listOf<Pair<Int, String>>()) }
    var showDialog by remember { mutableStateOf(false) }
    var meetingTime by remember { mutableStateOf("") }
    var meetingNotes by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Top row with one button
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Meeting")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom row with two buttons side by side
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onCancelButtonClicked,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(Res.string.cancel))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onNextButtonClicked(convertToMeetingList(meetings)) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(Res.string.next))
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                items(meetings) { meeting ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Meeting Time: ${meeting.first}" + " mins")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Meeting Notes: ${meeting.second}")
                    }
                }
            }
        }

        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Enter Meeting Details")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Meeting Time in Mins")
                        BasicTextField(
                            value = meetingTime,
                            onValueChange = { meetingTime = it },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Meeting Notes")
                        BasicTextField(
                            value = meetingNotes,
                            onValueChange = { meetingNotes = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = {
                                showDialog = false
                            }) {
                                Text("Cancel")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(onClick = {
                                val timeInMinutes = meetingTime.toIntOrNull()
                                if (timeInMinutes != null && meetingNotes.isNotBlank()) {
                                    meetings = meetings + Pair(timeInMinutes, meetingNotes)
                                    meetingTime = ""
                                    meetingNotes = ""
                                    showDialog = false
                                }
                            }) {
                                Text("OK")
                            }
                        }
                    }
                }
            }
        }
    }
}

fun convertToMeetingList(meetings: List<Pair<Int, String>>): List<MeetingRequest> {
    return meetings.map { meeting ->
        MeetingRequest(0, 0, meeting.first, meeting.second)
    }
}
