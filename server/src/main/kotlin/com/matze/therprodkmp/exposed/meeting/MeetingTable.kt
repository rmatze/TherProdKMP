package com.matze.therprodkmp.exposed.meeting

import com.matze.therprodkmp.exposed.workday.WorkdayTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

internal object MeetingTable : IntIdTable("meeting") {
    val timeInMeeting = integer("time_in_meeting")
    val meetingNotes = varchar("meeting_notes", 255)
    val workdayId = reference(
        "workday_id",
        WorkdayTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
}