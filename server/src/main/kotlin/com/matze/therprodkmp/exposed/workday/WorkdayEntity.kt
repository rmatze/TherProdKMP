package com.matze.therprodkmp.exposed.workday

import com.matze.therprodkmp.exposed.meeting.MeetingEntity
import com.matze.therprodkmp.exposed.meeting.MeetingTable
import com.matze.therprodkmp.exposed.meeting.toMeeting
import com.matze.therprodkmp.exposed.timesheet.TimesheetEntity
import com.matze.therprodkmp.exposed.timesheet.TimesheetTable
import com.matze.therprodkmp.exposed.timesheet.toTimesheet
import com.matze.therprodkmp.exposed.treatment.TreatmentEntity
import com.matze.therprodkmp.exposed.treatment.TreatmentTable
import com.matze.therprodkmp.exposed.treatment.toTreatment
import com.matze.therprodkmp.model.WorkdayResponse
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

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
    timesheets.map { it.toTimesheet(id.value) },
    meetings.map { it.toMeeting(id.value) },
    treatments.map { it.toTreatment(id.value) }
)