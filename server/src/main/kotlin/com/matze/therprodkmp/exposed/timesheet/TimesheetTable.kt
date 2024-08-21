package com.matze.therprodkmp.exposed.timesheet

import com.matze.therprodkmp.exposed.workday.WorkdayTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

internal object TimesheetTable : IntIdTable("timesheet") {
    val clockIn = varchar("clock_in", 50)
    val clockOut = varchar("clock_out", 50)
    val minsClockedIn = integer("mins_clocked_in")
    val workdayId = reference(
        "workday_id",
        WorkdayTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
}