package com.matze.therprodkmp.exposed.treatment

import com.matze.therprodkmp.exposed.workday.WorkdayEntity
import com.matze.therprodkmp.exposed.workday.WorkdayTable
import com.matze.therprodkmp.model.TreatmentRequest
import com.matze.therprodkmp.model.TreatmentResponse
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
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

class TreatmentEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TreatmentEntity>(TreatmentTable)

    var timeInMins by TreatmentTable.timeInMins
    var workday by WorkdayEntity referencedOn TreatmentTable.workdayId
}

fun TreatmentEntity.toTreatmentRequest(workdayId: Int) =
    TreatmentRequest(workdayId = workdayId, timeInMins = timeInMins)

fun TreatmentEntity.toTreatmentResponse(workdayId: Int) =
    TreatmentResponse(id.value, workdayId, timeInMins)