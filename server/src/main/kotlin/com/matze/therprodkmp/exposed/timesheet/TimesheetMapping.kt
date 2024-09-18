package com.matze.therprodkmp.exposed.timesheet

import com.matze.therprodkmp.exposed.workday.WorkdayEntity
import com.matze.therprodkmp.exposed.workday.WorkdayTable
import com.matze.therprodkmp.model.TimesheetRequest
import com.matze.therprodkmp.model.TimesheetResponse
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.timestamp


internal object TimesheetTable : IntIdTable("timesheet") {
    val clockIn = timestamp("clock_in")
    val clockOut = timestamp("clock_out").nullable()
    val minsClockedIn = integer("mins_clocked_in").nullable()
    val workdayId = reference(
        "workday_id",
        WorkdayTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
}

class TimesheetEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TimesheetEntity>(TimesheetTable)

    var clockIn: java.time.Instant by TimesheetTable.clockIn
    var clockOut: java.time.Instant? by TimesheetTable.clockOut
    var minsClockedIn by TimesheetTable.minsClockedIn
    var workday by WorkdayEntity referencedOn TimesheetTable.workdayId
}

fun TimesheetEntity.toTimesheetResponse(workdayId: Int) = TimesheetResponse(
    id.value,
    workdayId,
    clockIn = javaToKotlinxInstant(clockIn),
    clockOut = clockOut?.let { javaToKotlinxInstant(it) },
    minsClockedIn
)

fun TimesheetEntity.toTimesheetRequest(workdayId: Int) = TimesheetRequest(
    clockIn = javaToKotlinxInstant(clockIn),
    clockOut = clockOut?.let { javaToKotlinxInstant(it) },
    minsClockedIn = minsClockedIn
)

fun javaToKotlinxInstant(javaInstant: java.time.Instant): kotlinx.datetime.Instant {
    return kotlinx.datetime.Instant.fromEpochSeconds(javaInstant.epochSecond, javaInstant.nano)
}