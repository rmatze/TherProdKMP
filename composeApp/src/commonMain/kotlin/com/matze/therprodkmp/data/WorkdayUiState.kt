package com.matze.therprodkmp.data

import com.matze.therprodkmp.model.MeetingRequest
import com.matze.therprodkmp.model.TimesheetRequest
import com.matze.therprodkmp.model.TreatmentRequest

data class WorkdayUiState(
    val workdayId: Int = 0,
    val date: String = "",
    val treatmentRequests: List<TreatmentRequest> = listOf(),
    val meetingRequests: List<MeetingRequest> = listOf(),
    val timesheetRequests: List<TimesheetRequest> = listOf()
)