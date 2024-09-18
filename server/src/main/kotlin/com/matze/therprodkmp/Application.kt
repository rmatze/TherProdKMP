package com.matze.therprodkmp

import com.matze.therprodkmp.exposed.LocalSourceImpl
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val localSource = LocalSourceImpl(this)

    configureSerialization()
    configureRouting(localSource)
}

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            explicitNulls = false
        })
    }
}

internal fun Application.configureRouting(localSource: LocalSourceImpl) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            cause.printStackTrace()
            call.respondText(
                text = "${HttpStatusCode.BadRequest.value}: $cause. \nMinimum required fields:\n" + getResponseErrorMessage(
                    cause.message
                ),
                status = HttpStatusCode.BadRequest
            )

        }
    }
    install(Routing) {
        api(localSource)
    }
    install(CallLogging) {
        level = Level.INFO
    }
}

fun getResponseErrorMessage(cause: String?): String {
    return cause?.let {
        with(it) {
            when {
                contains("Workday") ->
                    "Add Workday\n" +
                            "{\n" +
                            " \"date\": \"01-23-4567\",\n" +
                            " \"timesheets\": [],\n" +
                            " \"meetings\": [],\n" +
                            " \"treatments\": []\n" +
                            "}"

                contains("Timesheet") ->
                    "Add Timesheet\n" +
                            "{\n" +
                            " \"workdayId\": 14,\n" +
                            " \"clockIn\": \"2024-09-13T08:30:00Z\"\n" +
                            "}"

                contains("Meeting") ->
                    "Add Meeting\n" +
                            "{\n" +
                            " \"workdayId\": 14,\n" +
                            " \"timeInMeeting\": 60,\n" +
                            " \"meetingNotes\": \"More meeting notes with 60 mins\"\n" +
                            "}"

                contains("Treatment") ->
                    "Add Treatment\n" +
                            "{\n" +
                            " \"workdayId\": 14,\n" +
                            " \"timeInMins\": 120\n" +
                            "}"

                else -> "Request Exception: $cause"
            }
        }
    } ?: "Request Exception"
}