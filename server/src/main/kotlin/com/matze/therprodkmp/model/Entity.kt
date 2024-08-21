package com.matze.therprodkmp.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkdayResponse(
    val id: Int = 0,
    val date: String,
    val timesheets: List<Timesheet>,
    val meetings: List<Meeting>,
    val treatments: List<Treatment>
)

@Serializable
data class WorkdayPostRequest(
    val id: Int = 0,
    val date: String,
    val timesheets: List<Timesheet>,
    val meetings: List<Meeting>,
    val treatments: List<Treatment>
)

@Serializable
data class Timesheet(
    val id: Int,
    val workdayId: Int,
    val clockIn: String,
    val clockOut: String,
    val minsClockedIn: Int
)

@Serializable
data class Meeting(
    val id: Int,
    val workdayId: Int,
    val timeInMeeting: Int,
    val meetingNotes: String
)

@Serializable
data class Treatment(
    val id: Int,
    val workdayId: Int,
    val timeInMins: Int
)