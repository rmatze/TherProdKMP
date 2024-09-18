package com.matze.therprodkmp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matze.therprodkmp.model.MeetingResponse
import com.matze.therprodkmp.model.TimesheetResponse
import com.matze.therprodkmp.model.TreatmentResponse
import com.matze.therprodkmp.model.WorkdayResponse
import com.matze.therprodkmp.util.roundToDecimals

@Composable
fun WorkdayDetailsScreen(
    viewModel: WorkdayViewModel
) {
    val workday = viewModel.workdayById.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchWorkdayById(viewModel.uiState.value.workdayId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Date: ${workday.value.date}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Timesheets:", style = MaterialTheme.typography.displaySmall)
        TimesheetList(timesheetResponse = workday.value.timesheetResponse)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Meetings:", style = MaterialTheme.typography.displaySmall)
        MeetingList(meetingResponse = workday.value.meetingResponse)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Treatments:", style = MaterialTheme.typography.displaySmall)
        TreatmentList(treatmentResponse = workday.value.treatmentResponse)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Productivity:", style = MaterialTheme.typography.displaySmall)
        Productivity(workday.value)
    }
}

@Composable
fun Productivity(workday: WorkdayResponse) {
    val timeSheetList = workday.timesheetResponse.toList()
    var timeWorked = 0
    timeSheetList.forEach {
        timeWorked += it.minsClockedIn ?: 0
    }
    var treatmentTime = 0
    workday.treatmentResponse.forEach {
        treatmentTime += it.timeInMins
    }
    var productivity = 0.0
    if (timeWorked > 0) {
        productivity = (treatmentTime / timeWorked.toDouble()) * 100
    }
    Text(
        text = "Productivity: ${productivity.toFloat().roundToDecimals(2)}%",
        modifier = Modifier
            .padding(8.dp)
    )
}

@Composable
fun TimesheetList(timesheetResponse: List<TimesheetResponse>) {
    LazyColumn {
        items(timesheetResponse) { timesheet ->
            TimesheetItem(timesheetResponse = timesheet)
        }
    }
}

@Composable
fun TimesheetItem(timesheetResponse: TimesheetResponse) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Clock In: ${timesheetResponse.clockIn}")
        if (timesheetResponse.clockOut != null) {
            Text(text = "Clock Out: ${timesheetResponse.clockOut}")
            Text(text = "Minutes Clocked In: ${timesheetResponse.minsClockedIn} mins")
        }
    }
}

@Composable
fun MeetingList(meetingResponse: List<MeetingResponse>) {
    LazyColumn {
        items(meetingResponse) { meeting ->
            MeetingItem(meetingResponse = meeting)
        }
    }
}

@Composable
fun MeetingItem(meetingResponse: MeetingResponse) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Time in Meeting: ${meetingResponse.timeInMeeting} mins")
        Text(text = "Notes: ${meetingResponse.meetingNotes}")
    }
}

@Composable
fun TreatmentList(treatmentResponse: List<TreatmentResponse>) {
    LazyColumn {
        items(treatmentResponse) { treatment ->
            TreatmentItem(treatmentResponse = treatment)
        }
    }
}

@Composable
fun TreatmentItem(treatmentResponse: TreatmentResponse) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Time in Treatment: ${treatmentResponse.timeInMins} mins")
    }
}