package com.matze.therprodkmp.exposed.timesheet

import com.matze.therprodkmp.exposed.workday.WorkdayEntity
import com.matze.therprodkmp.exposed.workday.WorkdayTable
import com.matze.therprodkmp.model.Timesheet
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

internal object TimesheetTable : IntIdTable("timesheet") {
    val clockIn = timestamp("clock_in")
    val clockOut = timestamp("clock_out").nullable()
    val minsClockedIn = integer("mins_clocked_in")
    val workdayId = reference(
        "workday_id",
        WorkdayTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
}

class TimesheetEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TimesheetEntity>(TimesheetTable)

    var clockIn by TimesheetTable.clockIn
    var clockOut: Instant? by TimesheetTable.clockOut
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