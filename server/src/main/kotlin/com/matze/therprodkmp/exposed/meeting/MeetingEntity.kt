package com.matze.therprodkmp.exposed.meeting

import com.matze.therprodkmp.exposed.workday.WorkdayEntity
import com.matze.therprodkmp.model.Meeting
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class MeetingEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MeetingEntity>(MeetingTable)

    var timeInMeeting by MeetingTable.timeInMeeting
    var meetingNotes by MeetingTable.meetingNotes
    var workday by WorkdayEntity referencedOn MeetingTable.workdayId
}

fun MeetingEntity.toMeeting(workdayId: Int) = Meeting(
    id.value, workdayId, timeInMeeting, meetingNotes
)