package com.matze.therprodkmp.model

import com.matze.therprodkmp.exposed.InstantSerializer
import com.matze.therprodkmp.exposed.InstantSerializerNullable
import kotlinx.serialization.Serializable
import java.time.Instant

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
    val id: Int?,
    val date: String,
    val timesheets: List<Timesheet>,
    val meetings: List<Meeting>,
    val treatments: List<Treatment>
)

@Serializable
data class Timesheet(
    val id: Int?,
    val workdayId: Int,
    @Serializable(with = InstantSerializer::class)
    val clockIn: Instant,
    @Serializable(with = InstantSerializerNullable::class)
    val clockOut: Instant? = null,
    val minsClockedIn: Int?
)

@Serializable
data class Meeting(
    val id: Int?,
    val workdayId: Int,
    val timeInMeeting: Int,
    val meetingNotes: String
)

@Serializable
data class Treatment(
    val id: Int?,
    val workdayId: Int,
    val timeInMins: Int
)