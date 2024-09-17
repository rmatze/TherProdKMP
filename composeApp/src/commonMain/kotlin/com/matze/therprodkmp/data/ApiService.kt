package com.matze.therprodkmp.data

import com.matze.therprodkmp.data.model.WorkdayPostRequest
import com.matze.therprodkmp.data.model.WorkdayResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

interface ApiService {
    suspend fun getWorkdays(): List<WorkdayResponse>
    suspend fun getWorkdayById(workdayId: Int): WorkdayResponse
    suspend fun addWorkday(workdayPostRequest: WorkdayPostRequest)
    suspend fun deleteWorkdayById(workdayId: Int)
}

class ApiServiceImpl : ApiService {
    @OptIn(ExperimentalSerializationApi::class)
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    override suspend fun getWorkdays(): List<WorkdayResponse> {
        try {
            // Make the POST request
            val response: HttpResponse = client.get("http://192.168.86.40:8080/workdays")

            // Handle the response based on status code
            when (response.status) {
                HttpStatusCode.OK -> {
                    println("Request was successful: ${response.status}")
                    val responseBody: String =
                        response.bodyAsText() // Get the response body if needed
                    println("Response body: $responseBody")
                }

                HttpStatusCode.BadRequest -> {
                    println("Bad Request: ${response.status}")
                }

                HttpStatusCode.Unauthorized -> {
                    println("Unauthorized: ${response.status}")
                }

                HttpStatusCode.Forbidden -> {
                    println("Forbidden: ${response.status}")
                }

                HttpStatusCode.InternalServerError -> {
                    println("Server Error: ${response.status}")
                }

                else -> {
                    println("Unhandled response status: ${response.status}")
                }
            }

            return response.body()
        } catch (e: Exception) {
            println("Error during request: ${e.message}")
        } finally {
//            client.close()
        }

        return emptyList()
    }

    override suspend fun getWorkdayById(workdayId: Int): WorkdayResponse {
        return client.get("http://192.168.86.40:8080/workdays/$workdayId").body()
    }

    override suspend fun addWorkday(workdayPostRequest: WorkdayPostRequest) {
        try {
            // Make the POST request
            val response: HttpResponse = client.post("http://192.168.86.40:8080/workdays") {
                contentType(ContentType.Application.Json)
                setBody(workdayPostRequest)  // Use setBody() to send the request body
            }

            // Handle the response based on status code
            when (response.status) {
                HttpStatusCode.OK -> {
                    println("Request was successful: ${response.status}")
                    val responseBody: String = response.body() // Get the response body if needed
                    println("Response body: $responseBody")
                }

                HttpStatusCode.BadRequest -> {
                    println("Bad Request: ${response.status}")
                }

                HttpStatusCode.Unauthorized -> {
                    println("Unauthorized: ${response.status}")
                }

                HttpStatusCode.Forbidden -> {
                    println("Forbidden: ${response.status}")
                }

                HttpStatusCode.InternalServerError -> {
                    println("Server Error: ${response.status}")
                }

                else -> {
                    println("Unhandled response status: ${response.status}")
                }
            }
        } catch (e: Exception) {
            println("Error during request: ${e.message}")
        } finally {
//            client.close()
        }
    }

    override suspend fun deleteWorkdayById(workdayId: Int) {
        client.delete("http://192.168.86.40:8080/workdays/$workdayId")
    }
}