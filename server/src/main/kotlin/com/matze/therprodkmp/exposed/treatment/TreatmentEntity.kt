package com.matze.therprodkmp.exposed.treatment

import com.matze.therprodkmp.exposed.workday.WorkdayEntity
import com.matze.therprodkmp.model.Treatment
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TreatmentEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TreatmentEntity>(TreatmentTable)

    var timeInMins by TreatmentTable.timeInMins
    var workday by WorkdayEntity referencedOn TreatmentTable.workdayId
}

fun TreatmentEntity.toTreatment(workdayId: Int) = Treatment(id.value, workdayId, timeInMins)