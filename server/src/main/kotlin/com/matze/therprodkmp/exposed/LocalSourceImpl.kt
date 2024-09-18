package com.matze.therprodkmp.exposed

import com.matze.therprodkmp.exposed.meeting.MeetingEntity
import com.matze.therprodkmp.exposed.meeting.MeetingTable
import com.matze.therprodkmp.exposed.timesheet.TimesheetEntity
import com.matze.therprodkmp.exposed.timesheet.TimesheetTable
import com.matze.therprodkmp.exposed.treatment.TreatmentEntity
import com.matze.therprodkmp.exposed.treatment.TreatmentTable
import com.matze.therprodkmp.exposed.workday.WorkdayEntity
import com.matze.therprodkmp.exposed.workday.WorkdayTable
import com.matze.therprodkmp.exposed.workday.toWorkdayResponse
import com.matze.therprodkmp.model.MeetingRequest
import com.matze.therprodkmp.model.TimesheetRequest
import com.matze.therprodkmp.model.TreatmentRequest
import com.matze.therprodkmp.model.WorkdayRequest
import com.matze.therprodkmp.model.WorkdayResponse
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.util.logging.KtorSimpleLogger
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.Duration
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

    override suspend fun addWorkday(workdayRequest: WorkdayRequest) =
        withContext(dispatcher) {
            withContext(dispatcher) {
                val workdayId = transaction {
                    WorkdayEntity.new {
                        date = workdayRequest.date
                    }.id.value
                }

                workdayRequest.timesheets.forEach {
                    addTimesheet(workdayId, it)
                }

                workdayRequest.meetings.forEach {
                    addMeeting(workdayId, it)
                }

                workdayRequest.treatments.forEach {
                    addTreatment(workdayId, it)
                }

                workdayId
            }
        }

    override suspend fun addTimesheet(workdayId: Int, timesheetRequest: TimesheetRequest) =
        withContext(dispatcher) {
            LOGGER.info("Test Log")
            transaction {
                val workday = WorkdayEntity[workdayId]
                TimesheetEntity.new {
                    clockIn = kotlinxToJavaInstant(timesheetRequest.clockIn)
                    clockOut = timesheetRequest.clockOut?.let { kotlinxToJavaInstant(it) }
                    minsClockedIn =
                        timesheetRequest.clockOut?.let {
                            minutesDifference(
                                kotlinxToJavaInstant(
                                    timesheetRequest.clockIn
                                ), kotlinxToJavaInstant(it)
                            )
                        }!!
                    this.workday = workday
                }.id.value
            }
        }

    override suspend fun addMeeting(workdayId: Int, meetingRequest: MeetingRequest) =
        withContext(dispatcher) {
            transaction {
                val workday = WorkdayEntity[workdayId]
                MeetingEntity.new {
                    timeInMeeting = meetingRequest.timeInMeeting
                    meetingNotes = meetingRequest.meetingNotes
                    this.workday = workday
                }.id.value
            }
        }

    override suspend fun addTreatment(workdayId: Int, treatmentRequest: TreatmentRequest) =
        withContext(dispatcher) {
            transaction {
                val workday = WorkdayEntity[workdayId]
                TreatmentEntity.new {
                    timeInMins = treatmentRequest.timeInMins
                    this.workday = workday
                }.id.value
            }
        }

    override suspend fun updateWorkday(workdayId: Int, workdayRequest: WorkdayRequest) =
        withContext(dispatcher) {
            withContext(dispatcher) {
                var rows = transaction {
                    WorkdayTable.update({ WorkdayTable.id eq workdayId }) {
                        it[date] = workdayRequest.date
                    }
                }

                workdayRequest.timesheets.forEach {
                    rows += updateTimesheet(workdayId, it)
                }

                workdayRequest.meetings.forEach {
                    updateMeeting(workdayId, it)
                }

                workdayRequest.treatments.forEach {
                    updateTreatment(workdayId, it)
                }

                rows
            }
        }


    override suspend fun updateTimesheet(workdayId: Int, timesheetRequest: TimesheetRequest) =
        withContext(dispatcher) {
            val rows = transaction {
                TimesheetTable.update({ TimesheetTable.workdayId eq workdayId }) {
                    it[clockIn] = kotlinxToJavaInstant(timesheetRequest.clockIn)
                    it[clockOut] = timesheetRequest.clockOut?.let { kotlinxToJavaInstant(it) }
                    it[minsClockedIn] = timesheetRequest.minsClockedIn ?: 0
                }
            }

            rows
        }

    override suspend fun updateMeeting(workdayId: Int, meetingRequest: MeetingRequest) =
        withContext(dispatcher) {
            val rows = transaction {
                MeetingTable.update({ MeetingTable.workdayId eq workdayId }) {
                    it[timeInMeeting] = meetingRequest.timeInMeeting
                    it[meetingNotes] = meetingRequest.meetingNotes
                }
            }

            rows
        }

    override suspend fun updateTreatment(workdayId: Int, treatmentRequest: TreatmentRequest) =
        withContext(dispatcher) {
            val rows = transaction {
                TreatmentTable.update({ TreatmentTable.workdayId eq workdayId }) {
                    it[timeInMins] = treatmentRequest.timeInMins
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

    fun kotlinxToJavaInstant(kotlinxInstant: Instant): java.time.Instant {
        return java.time.Instant.ofEpochSecond(
            kotlinxInstant.epochSeconds,
            kotlinxInstant.nanosecondsOfSecond.toLong()
        )
    }

    fun minutesDifference(start: java.time.Instant, end: java.time.Instant): Int {
        // Calculate the Duration between the two LocalDateTime instances
        val duration = Duration.between(start, end)

        // Return the difference in minutes
        return duration.toMinutes().toInt()
    }
}