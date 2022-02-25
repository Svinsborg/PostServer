package ru.hell.server

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.hell.server.route.*
import java.text.SimpleDateFormat

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CallLogging) {
        format { call ->
            val duration = System.currentTimeMillis()
            val date = getDateTime(duration)
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val httpURL = call.request.origin.uri
            val host = call.request.origin.host
            val port = call.request.origin.port
            val scheme = call.request.origin.scheme
            val httpV = call.request.httpVersion
            val httpHeaders = call.request.origin.remoteHost
            val userAgent = call.request.headers["User-Agent"]
            """
-------- Start ---------
=> Date: $date
=> Remote Host: $httpHeaders
=> User agent: $userAgent
=> HTTP method: $httpMethod
=> HTTP URL: "$scheme://$host:$port$httpURL"
=> Status: $status
=> HTTP version: $httpV
--------  END  --------"""
        }
    }

    install(StatusPages) {
        status(HttpStatusCode.NotFound) {
            call.respond(TextContent("${it.value} ${it.description}", ContentType.Text.Plain.withCharset(Charsets.UTF_8), it))
        }
        exception<NotImplementedError> { e ->
            call.respond(HttpStatusCode.NotImplemented, Error("Error"))
            throw e
        }
        exception<ParameterConversionException> { e ->
            call.respond(HttpStatusCode.BadRequest)
            throw e
        }
        exception<Throwable> { e ->
            call.respond(HttpStatusCode.InternalServerError)
            throw e
        }
    }

    install(Routing){
        v1()
    }
}

private fun getDateTime(s: Long): String? {
    try {
        val sdf = SimpleDateFormat("dd/MM/yyyy kk:mm:ss")
        return sdf.format(s)
    } catch (e: Exception) {
        return e.toString()
    }
}
