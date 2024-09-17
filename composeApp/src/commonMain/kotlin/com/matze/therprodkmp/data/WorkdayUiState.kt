package com.matze.therprodkmp.data

import com.matze.therprodkmp.data.model.Meeting
import com.matze.therprodkmp.data.model.Timesheet
import com.matze.therprodkmp.data.model.Treatment

data class WorkdayUiState(
    val workdayId: Int = 0,
    val date: String = "",
    val treatments: List<Treatment> = listOf(),
    val meetings: List<Meeting> = listOf(),
    val timesheets: List<Timesheet> = listOf()
)