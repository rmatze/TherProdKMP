package com.matze.therprodkmp.model

import com.matze.therprodkmp.utils.InstantSerializer
import com.matze.therprodkmp.utils.InstantSerializerNullable
import kotlinx.datetime.Instant

import kotlinx.serialization.Serializable

@Serializable
data class WorkdayResponse(
    val id: Int,
    val date: String,
    val timesheets: List<TimesheetResponse>,
    val meetings: List<MeetingResponse>,
    val treatments: List<TreatmentResponse>
)

@Serializable
data class TimesheetResponse(
    val id: Int,
    val workdayId: Int,
    @Serializable(with = InstantSerializer::class)
    val clockIn: Instant,
    @Serializable(with = InstantSerializerNullable::class)
    val clockOut: Instant? = null,
    val minsClockedIn: Int? = null
)

@Serializable
data class MeetingResponse(
    val id: Int,
    val workdayId: Int,
    val timeInMeeting: Int,
    val meetingNotes: String? = null
)

@Serializable
data class TreatmentResponse(
    val id: Int,
    val workdayId: Int,
    val timeInMins: Int
)