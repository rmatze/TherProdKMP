package com.matze.therprodkmp

import com.example.exposed.LocalSource
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post

internal fun Routing.api(localSource: LocalSource) {
    workdays(localSource)
}

private fun Routing.workdays(localSource: LocalSource) {
    get("/workdays") {
        localSource.getWorkdays().let {
            call.respond(it)
        }
    }
    get("/workdays/{workdayId}") {
        val workdayId = call.parameters["workdayId"]!!.toInt()
        localSource.getWorkday(workdayId).let {
            call.respond(it)
        }
    }

    post("/workdays") {
        val workdayId = localSource.addWorkday(call.receive())
        call.respond(workdayId)
    }

    post("/workdays/{workdayId}") {
        val workdayId = call.parameters["workdayId"]!!.toInt()
        localSource.updateWorkday(workdayId, call.receive()).let {
            call.respond(it)
        }
    }

    post("/workdays/treatment/{workdayId}") {
        val workdayId = call.parameters["workdayId"]!!.toInt()
        localSource.addTreatment(workdayId, call.receive())
        call.respond(it)
    }

    post("/workdays/timesheet/{workdayId}") {
        val workdayId = call.parameters["workdayId"]!!.toInt()
        localSource.addTimesheet(workdayId, call.receive())
        call.respond(it)
    }

    post("/workdays/meeting/{workdayId}") {
        val workdayId = call.parameters["workdayId"]!!.toInt()
        localSource.addMeeting(workdayId, call.receive())
        call.respond(it)
    }

    delete("/workdays/{workdayId}") {
        val workdayId = call.parameters["workdayId"]!!.toInt()
        localSource.deleteWorkday(workdayId).let {
            call.respond(it)
        }
    }

    delete("/workdays/timesheet/{timesheetId}") {
        val timesheetId = call.parameters["timesheetId"]!!.toInt()
        localSource.deleteTimesheet(timesheetId).let {
            call.respond(it)
        }
    }

    delete("/workdays/meeting/{meetingId}") {
        val meetingId = call.parameters["meetingId"]!!.toInt()
        localSource.deleteMeeting(meetingId).let {
            call.respond(it)
        }
    }

    delete("/workdays/treatment/{treatmentId}") {
        val treatmentId = call.parameters["treatmentId"]!!.toInt()
        localSource.deleteTreatment(treatmentId).let {
            call.respond(it)
        }
    }
}