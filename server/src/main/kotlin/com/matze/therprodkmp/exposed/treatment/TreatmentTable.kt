package com.matze.therprodkmp.exposed.treatment

import com.matze.therprodkmp.exposed.workday.WorkdayTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

internal object TreatmentTable : IntIdTable("treatment") {
    val timeInMins = integer("time_in_mins")
    val workdayId = reference(
        "workday_id",
        WorkdayTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
}