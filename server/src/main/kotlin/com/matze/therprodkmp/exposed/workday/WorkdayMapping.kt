package com.matze.therprodkmp.exposed.workday

import com.matze.therprodkmp.exposed.meeting.MeetingEntity
import com.matze.therprodkmp.exposed.meeting.MeetingTable
import com.matze.therprodkmp.exposed.meeting.toMeetingRequest
import com.matze.therprodkmp.exposed.meeting.toMeetingResponse
import com.matze.therprodkmp.exposed.timesheet.TimesheetEntity
import com.matze.therprodkmp.exposed.timesheet.TimesheetTable
import com.matze.therprodkmp.exposed.timesheet.toTimesheetRequest
import com.matze.therprodkmp.exposed.timesheet.toTimesheetResponse
import com.matze.therprodkmp.exposed.treatment.TreatmentEntity
import com.matze.therprodkmp.exposed.treatment.TreatmentTable
import com.matze.therprodkmp.exposed.treatment.toTreatmentRequest
import com.matze.therprodkmp.exposed.treatment.toTreatmentResponse
import com.matze.therprodkmp.model.WorkdayRequest
import com.matze.therprodkmp.model.WorkdayResponse
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

internal object WorkdayTable : IntIdTable("workday") {
    val date = varchar("date", 100)
}

class WorkdayEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WorkdayEntity>(WorkdayTable)

    var date by WorkdayTable.date
    val timesheets by TimesheetEntity referrersOn TimesheetTable.workdayId
    val meetings by MeetingEntity referrersOn MeetingTable.workdayId
    val treatments by TreatmentEntity referrersOn TreatmentTable.workdayId
}

fun WorkdayEntity.toWorkdayResponse() = WorkdayResponse(
    id.value,
    date,
    timesheets.map { it.toTimesheetResponse(id.value) },
    meetings.map { it.toMeetingResponse(id.value) },
    treatments.map { it.toTreatmentResponse(id.value) }
)

fun WorkdayEntity.toWorkdayRequest() = WorkdayRequest(
    date = date,
    timesheets = timesheets.map { it.toTimesheetRequest(id.value) },
    meetings = meetings.map { it.toMeetingRequest(id.value) },
    treatments = treatments.map { it.toTreatmentRequest(id.value) }
)