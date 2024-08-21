package com.matze.therprodkmp.exposed.timesheet

import com.matze.therprodkmp.exposed.workday.WorkdayEntity
import com.matze.therprodkmp.model.Timesheet
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TimesheetEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TimesheetEntity>(TimesheetTable)

    var clockIn by TimesheetTable.clockIn
    var clockOut by TimesheetTable.clockOut
    var minsClockedIn by TimesheetTable.minsClockedIn
    var workday by WorkdayEntity referencedOn TimesheetTable.workdayId
}

fun TimesheetEntity.toTimesheet(workdayId: Int) = Timesheet(
    id.value,
    workdayId,
    clockIn,
    clockOut,
    minsClockedIn
)