package com.matze.therprodkmp.data.model

import com.matze.therprodkmp.util.InstantSerializer
import com.matze.therprodkmp.util.InstantSerializerNullable
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class WorkdayResponse(
    val id: Int,
    val date: String,
    val timesheets: List<Timesheet>,
    val meetings: List<Meeting>,
    val treatments: List<Treatment>
)

@Serializable
data class WorkdayPostRequest(
    val id: Int? = 0,
    val date: String,
    val timesheets: List<Timesheet>,
    val meetings: List<Meeting>,
    val treatments: List<Treatment>
)

@Serializable
data class Timesheet(
    val id: Int? = 0,
    val workdayId: Int,
    @Serializable(with = InstantSerializer::class)
    val clockIn: Instant,
    @Serializable(with = InstantSerializerNullable::class)
    val clockOut: Instant? = null,
    val minsClockedIn: Int?
)

@Serializable
data class Meeting(
    val id: Int? = 0,
    val workdayId: Int,
    val timeInMeeting: Int,
    val meetingNotes: String
)

@Serializable
data class Treatment(
    val id: Int? = 0,
    val workdayId: Int,
    val timeInMins: Int
)