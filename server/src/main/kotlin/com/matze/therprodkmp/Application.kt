package com.matze.therprodkmp

import com.example.exposed.LocalSourceImpl
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
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
    install(Routing) {
        api(localSource)
    }
    install(CallLogging) {
        level = Level.INFO
    }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(
                text = "${HttpStatusCode.BadRequest.value}: $cause.  Minimum required fields:" +
                        "\n{\n" +
                        "    \"date\": \"\",\n" +
                        "    \"timesheets\": [],\n" +
                        "    \"meetings\": [],\n" +
                        "    \"treatments\": []\n" +
                        "}", status = HttpStatusCode.BadRequest
            )
        }
    }
}