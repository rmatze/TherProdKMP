package com.matze.therprodkmp.exposed

import com.matze.therprodkmp.model.MeetingRequest
import com.matze.therprodkmp.model.TimesheetRequest
import com.matze.therprodkmp.model.TreatmentRequest
import com.matze.therprodkmp.model.WorkdayRequest
import com.matze.therprodkmp.model.WorkdayResponse

internal interface LocalSource {
    suspend fun getWorkdays(): List<WorkdayResponse>
    suspend fun getWorkday(workdayId: Int): WorkdayResponse

    suspend fun addWorkday(workdayRequest: WorkdayRequest): Int
    suspend fun addTimesheet(workdayId: Int, timesheetRequest: TimesheetRequest): Int
    suspend fun addMeeting(workdayId: Int, meetingRequest: MeetingRequest): Int
    suspend fun addTreatment(workdayId: Int, treatmentRequest: TreatmentRequest): Int

    suspend fun updateWorkday(workdayId: Int, workdayRequest: WorkdayRequest): Int
    suspend fun updateTimesheet(workdayId: Int, timesheetRequest: TimesheetRequest): Int
    suspend fun updateMeeting(workdayId: Int, meetingRequest: MeetingRequest): Int
    suspend fun updateTreatment(workdayId: Int, treatmentRequest: TreatmentRequest): Int

    suspend fun deleteWorkday(workdayId: Int): Int
    suspend fun deleteTimesheet(timesheetId: Int): Int
    suspend fun deleteMeeting(meetingId: Int): Int
    suspend fun deleteTreatment(treatmentId: Int): Int
}