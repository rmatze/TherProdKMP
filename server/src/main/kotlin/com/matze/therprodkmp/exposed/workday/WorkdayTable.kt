package com.matze.therprodkmp.exposed.workday

import org.jetbrains.exposed.dao.id.IntIdTable

internal object WorkdayTable : IntIdTable("workday") {
    val date = varchar("date", 100)
}