package com.example.exposed

import com.matze.therprodkmp.exposed.meeting.MeetingEntity
import com.matze.therprodkmp.exposed.meeting.MeetingTable
import com.matze.therprodkmp.exposed.timesheet.TimesheetEntity
import com.matze.therprodkmp.exposed.timesheet.TimesheetTable
import com.matze.therprodkmp.exposed.treatment.TreatmentEntity
import com.matze.therprodkmp.exposed.treatment.TreatmentTable
import com.matze.therprodkmp.exposed.workday.WorkdayEntity
import com.matze.therprodkmp.exposed.workday.WorkdayTable
import com.matze.therprodkmp.exposed.workday.toWorkdayResponse
import com.matze.therprodkmp.model.Meeting
import com.matze.therprodkmp.model.Timesheet
import com.matze.therprodkmp.model.Treatment
import com.matze.therprodkmp.model.WorkdayPostRequest
import com.matze.therprodkmp.model.WorkdayResponse
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import io.ktor.util.logging.*
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import kotlin.coroutines.CoroutineContext

internal val LOGGER = KtorSimpleLogger("com.example.RequestTracePlugin")

internal class LocalSourceImpl(
    application: Application
) : LocalSource {
    private val dispatcher: CoroutineContext

    init {
        val config = application.environment.config.config("database")
        val dbUser = config.property("user").getString()
        val dbPassword = config.property("password").getString()
        val dbName = config.property("db_name").getString()
        val url = "jdbc:postgresql://localhost:5432/$dbName"
        val driver = config.property("driver").getString()
        val poolSize = config.property("poolSize").getString().toInt()
        application.log.info("Connecting to db at $url")

        dispatcher = newFixedThreadPoolContext(poolSize, "database-pool")
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = url
            maximumPoolSize = poolSize
            driverClassName = driver
            username = dbUser
            password = dbPassword
            validate()
        }

        Database.connect(HikariDataSource(hikariConfig))

        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                WorkdayTable,
                TimesheetTable,
                MeetingTable,
                TreatmentTable
            )
        }
    }

    override suspend fun getWorkdays(): List<WorkdayResponse> = withContext(dispatcher) {
        transaction {
            WorkdayEntity.all().map { it.toWorkdayResponse() }
        }
    }

    override suspend fun getWorkday(workdayId: Int): WorkdayResponse = withContext(dispatcher) {
        transaction {
            WorkdayEntity[workdayId].toWorkdayResponse()
        }
    }

    override suspend fun addWorkday(workdayPostRequest: WorkdayPostRequest) = withContext(dispatcher) {
        withContext(dispatcher) {
            val workdayId = transaction {
                WorkdayEntity.new {
                    date = workdayPostRequest.date
                }.id.value
            }

            workdayPostRequest.timesheets.forEach {
                addTimesheet(workdayId, it)
            }

            workdayPostRequest.meetings.forEach {
                addMeeting(workdayId, it)
            }

            workdayPostRequest.treatments.forEach {
                addTreatment(workdayId, it)
            }

            workdayId
        }
    }

    override suspend fun addTimesheet(workdayId: Int, timesheet: Timesheet) = withContext(dispatcher) {
        LOGGER.info("Test Log")
        transaction {
            val workday = WorkdayEntity[workdayId]
            TimesheetEntity.new {
                clockIn = timesheet.clockIn
                clockOut = timesheet.clockOut
                minsClockedIn = timesheet.minsClockedIn
                this.workday = workday
            }.id.value
        }
    }

    override suspend fun addMeeting(workdayId: Int, meeting: Meeting) = withContext(dispatcher) {
        transaction {
            val workday = WorkdayEntity[workdayId]
            MeetingEntity.new {
                timeInMeeting = meeting.timeInMeeting
                meetingNotes = meeting.meetingNotes
                this.workday = workday
            }.id.value
        }
    }

    override suspend fun addTreatment(workdayId: Int, treatment: Treatment) = withContext(dispatcher) {
        transaction {
            val workday = WorkdayEntity[workdayId]
            TreatmentEntity.new {
                timeInMins = treatment.timeInMins
                this.workday = workday
            }.id.value
        }
    }

    override suspend fun updateWorkday(workdayId: Int, workdayPostRequest: WorkdayPostRequest) =
        withContext(dispatcher) {
            withContext(dispatcher) {
                var rows = transaction {
                    WorkdayTable.update({ WorkdayTable.id eq workdayId }) {
                        it[date] = workdayPostRequest.date
                    }
                }

                workdayPostRequest.timesheets.forEach {
                    rows += updateTimesheet(workdayId, it)
                }

                workdayPostRequest.meetings.forEach {
                    updateMeeting(workdayId, it)
                }

                workdayPostRequest.treatments.forEach {
                    updateTreatment(workdayId, it)
                }

                rows
            }
        }


    override suspend fun updateTimesheet(workdayId: Int, timesheet: Timesheet) = withContext(dispatcher) {
        val rows = transaction {
            TimesheetTable.update({ TimesheetTable.workdayId eq workdayId }) {
                it[clockIn] = timesheet.clockIn
                it[clockOut] = timesheet.clockOut
                it[minsClockedIn] = timesheet.minsClockedIn
            }
        }

        rows
    }

    override suspend fun updateMeeting(workdayId: Int, meeting: Meeting) = withContext(dispatcher) {
        val rows = transaction {
            MeetingTable.update({ MeetingTable.workdayId eq workdayId }) {
                it[timeInMeeting] = meeting.timeInMeeting
                it[meetingNotes] = meeting.meetingNotes
            }
        }

        rows
    }

    override suspend fun updateTreatment(workdayId: Int, treatment: Treatment) = withContext(dispatcher) {
        val rows = transaction {
            TreatmentTable.update({ TreatmentTable.workdayId eq workdayId }) {
                it[timeInMins] = treatment.timeInMins
            }
        }

        rows
    }

    override suspend fun deleteWorkday(workdayId: Int) = withContext(dispatcher) {
        val rows = transaction {
            WorkdayTable.deleteWhere { id eq workdayId }
        }

        rows
    }

    override suspend fun deleteTimesheet(timesheetId: Int) = withContext(dispatcher) {
        val rows = transaction {
            TimesheetTable.deleteWhere { id eq timesheetId }
        }

        rows
    }

    override suspend fun deleteMeeting(meetingId: Int) = withContext(dispatcher) {
        val rows = transaction {
            MeetingTable.deleteWhere { id eq meetingId }
        }

        rows
    }

    override suspend fun deleteTreatment(treatmentId: Int) = withContext(dispatcher) {
        val rows = transaction {
            TreatmentTable.deleteWhere { id eq treatmentId }
        }

        rows
    }
}