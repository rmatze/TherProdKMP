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
import com.matze.therprodkmp.data.model.Meeting
import com.matze.therprodkmp.data.model.Timesheet
import com.matze.therprodkmp.data.model.Treatment
import com.matze.therprodkmp.data.model.WorkdayResponse
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
        TimesheetList(timesheets = workday.value.timesheets)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Meetings:", style = MaterialTheme.typography.displaySmall)
        MeetingList(meetings = workday.value.meetings)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Treatments:", style = MaterialTheme.typography.displaySmall)
        TreatmentList(treatments = workday.value.treatments)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Productivity:", style = MaterialTheme.typography.displaySmall)
        Productivity(workday.value)
    }
}

@Composable
fun Productivity(workday: WorkdayResponse) {
    val timeSheetList = workday.timesheets.toList()
    var timeWorked = 0
    timeSheetList.forEach {
        timeWorked += it.minsClockedIn ?: 0
    }
    var treatmentTime = 0
    workday.treatments.forEach {
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
fun TimesheetList(timesheets: List<Timesheet>) {
    LazyColumn {
        items(timesheets) { timesheet ->
            TimesheetItem(timesheet = timesheet)
        }
    }
}

@Composable
fun TimesheetItem(timesheet: Timesheet) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Clock In: ${timesheet.clockIn}")
        if(timesheet.clockOut != null) {
            Text(text = "Clock Out: ${timesheet.clockOut}")
            Text(text = "Minutes Clocked In: ${timesheet.minsClockedIn} mins")
        }
    }
}

@Composable
fun MeetingList(meetings: List<Meeting>) {
    LazyColumn {
        items(meetings) { meeting ->
            MeetingItem(meeting = meeting)
        }
    }
}

@Composable
fun MeetingItem(meeting: Meeting) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Time in Meeting: ${meeting.timeInMeeting} mins")
        Text(text = "Notes: ${meeting.meetingNotes}")
    }
}

@Composable
fun TreatmentList(treatments: List<Treatment>) {
    LazyColumn {
        items(treatments) { treatment ->
            TreatmentItem(treatment = treatment)
        }
    }
}

@Composable
fun TreatmentItem(treatment: Treatment) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Time in Treatment: ${treatment.timeInMins} mins")
    }
}