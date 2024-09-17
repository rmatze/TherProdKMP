package com.matze.therprodkmp.exposed.meeting

import com.matze.therprodkmp.exposed.workday.WorkdayEntity
import com.matze.therprodkmp.exposed.workday.WorkdayTable
import com.matze.therprodkmp.model.Meeting
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
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

class MeetingEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MeetingEntity>(MeetingTable)

    var timeInMeeting by MeetingTable.timeInMeeting
    var meetingNotes by MeetingTable.meetingNotes
    var workday by WorkdayEntity referencedOn MeetingTable.workdayId
}

fun MeetingEntity.toMeeting(workdayId: Int) = Meeting(
    id.value, workdayId, timeInMeeting, meetingNotes
)