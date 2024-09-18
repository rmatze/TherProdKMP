package com.matze.therprodkmp.model

import com.matze.therprodkmp.utils.InstantSerializer
import com.matze.therprodkmp.utils.InstantSerializerNullable
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class WorkdayRequest(
    val id: Int? = 0,
    val date: String,
    val timesheets: List<TimesheetRequest>,
    val meetings: List<MeetingRequest>,
    val treatments: List<TreatmentRequest>
)

@Serializable
data class TimesheetRequest(
    val id: Int? = 0,
    val workdayId: Int? = 0,
    @Serializable(with = InstantSerializer::class)
    val clockIn: Instant,
    @Serializable(with = InstantSerializerNullable::class)
    val clockOut: Instant? = null,
    val minsClockedIn: Int? = null
)

@Serializable
data class MeetingRequest(
    val id: Int? = 0,
    val workdayId: Int? = 0,
    val timeInMeeting: Int,
    val meetingNotes: String? = null
)

@Serializable
data class TreatmentRequest(
    val id: Int? = 0,
    val workdayId: Int? = 0,
    val timeInMins: Int
)

fun TimesheetRequest.toResponse(): TimesheetResponse {
    return TimesheetResponse(
        id = this.id ?: 0,  // Provide a default if null
        workdayId = this.workdayId ?: 0,  // Provide a default if null
        clockIn = this.clockIn,
        clockOut = this.clockOut,
        minsClockedIn = this.minsClockedIn
    )
}

fun MeetingRequest.toResponse(): MeetingResponse {
    return MeetingResponse(
        id = this.id ?: 0,  // Provide a default if null
        workdayId = this.workdayId ?: 0,  // Provide a default if null
        timeInMeeting = this.timeInMeeting,
        meetingNotes = this.meetingNotes
    )
}

fun TreatmentRequest.toResponse(): TreatmentResponse {
    return TreatmentResponse(
        id = this.id ?: 0,  // Provide a default if null
        workdayId = this.workdayId ?: 0,  // Provide a default if null
        timeInMins = this.timeInMins,
    )
}