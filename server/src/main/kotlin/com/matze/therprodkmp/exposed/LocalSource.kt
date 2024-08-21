package com.example.exposed

import com.matze.therprodkmp.model.Meeting
import com.matze.therprodkmp.model.Timesheet
import com.matze.therprodkmp.model.Treatment
import com.matze.therprodkmp.model.WorkdayPostRequest
import com.matze.therprodkmp.model.WorkdayResponse

internal interface LocalSource {
    suspend fun getWorkdays(): List<WorkdayResponse>
    suspend fun getWorkday(workdayId: Int): WorkdayResponse

    suspend fun addWorkday(workdayPostRequest: WorkdayPostRequest): Int
    suspend fun addTimesheet(workdayId: Int, timesheet: Timesheet): Int
    suspend fun addMeeting(workdayId: Int, meeting: Meeting): Int
    suspend fun addTreatment(workdayId: Int, treatment: Treatment): Int

    suspend fun updateWorkday(workdayId: Int, workdayPostRequest: WorkdayPostRequest): Int
    suspend fun updateTimesheet(workdayId: Int, timesheet: Timesheet): Int
    suspend fun updateMeeting(workdayId: Int, meeting: Meeting): Int
    suspend fun updateTreatment(workdayId: Int, treatment: Treatment): Int

    suspend fun deleteWorkday(workdayId: Int): Int
    suspend fun deleteTimesheet(timesheetId: Int): Int
    suspend fun deleteMeeting(meetingId: Int): Int
    suspend fun deleteTreatment(treatmentId: Int): Int
}