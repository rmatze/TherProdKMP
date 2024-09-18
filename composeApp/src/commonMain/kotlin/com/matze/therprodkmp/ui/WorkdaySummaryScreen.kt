package com.matze.therprodkmp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matze.therprodkmp.data.WorkdayUiState
import com.matze.therprodkmp.model.toResponse
import com.matze.therprodkmp.util.roundToDecimals
import org.jetbrains.compose.resources.stringResource
import therprodkmp.composeapp.generated.resources.Res
import therprodkmp.composeapp.generated.resources.cancel
import therprodkmp.composeapp.generated.resources.submit

@Composable
fun WorkdaySummaryScreen(
    workdayUiState: WorkdayUiState,
    onFinishButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Date: ${workdayUiState.date}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Timesheets:", style = MaterialTheme.typography.displaySmall)
        TimesheetList(timesheetResponse = workdayUiState.timesheetRequests.map { it.toResponse() })

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Meetings:", style = MaterialTheme.typography.displaySmall)
        MeetingList(meetingResponse = workdayUiState.meetingRequests.map { it.toResponse() })

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Treatments:", style = MaterialTheme.typography.displaySmall)
        TreatmentList(treatmentResponse = workdayUiState.treatmentRequests.map { it.toResponse() })

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Productivity:", style = MaterialTheme.typography.displaySmall)
        Productivity(workdayUiState)

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
                enabled = true,
                onClick = onFinishButtonClicked
            ) {
                Text(stringResource(Res.string.submit))
            }
        }
    }
}

@Composable
fun Productivity(workdayUiState: WorkdayUiState) {
    val timeSheetList = workdayUiState.timesheetRequests.toList()
    var timeWorked = 0
    timeSheetList.forEach {
        timeWorked += it.minsClockedIn ?: 0
    }
    var treatmentTime = 0
    workdayUiState.treatmentRequests.forEach {
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