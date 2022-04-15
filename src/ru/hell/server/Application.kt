package ru.hell.server

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.plugins.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.server.routing.Routing
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.response.*
import org.kodein.di.bindEagerSingleton
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.ktor.di
import org.kodein.di.with
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import ru.hell.server.repository.*
import ru.hell.server.route.RoutingV1
import ru.hell.server.service.FileService
import ru.hell.server.service.JWTTokenService
import ru.hell.server.service.PostService
import ru.hell.server.service.UserService
import java.text.SimpleDateFormat
import javax.naming.ConfigurationException


fun main(args: Array<String>) {
    EngineMain.main(args)
}

//@Suppress("unused") // Referenced in application.conf
fun Application.module() {
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
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(
                text = "404: Page Not Found",
                status = status)
        }
        status(HttpStatusCode.NotImplemented){ call, status ->
            call.respondText(
                text = "501: Not Implemented",
                status = status)
        }
        status(HttpStatusCode.InternalServerError) { call, status ->
            call.respondText(
                text = "500: Internal Server Error",
                status = status)
        }

        status (HttpStatusCode.BadRequest){ call, status ->
        call.respondText(
            text = "400: Bad Request",
            status = status)
        }
    }

    di {
        constant(tag = "upload-dir") with (environment.config.propertyOrNull("ncraft.upload.dir")?.getString()
            ?: throw ConfigurationException("Upload dir is not specified"))
        bindEagerSingleton<PostRepository> { PostRepositoryImp() }
        bindEagerSingleton<FileRepository> { FileRepositoryImp(instance(tag = "upload-dir")) }
        bindEagerSingleton<UserRepository> { UserRepositoryImp() }
        bindEagerSingleton { JWTTokenService() }
        bindEagerSingleton<PasswordEncoder> { BCryptPasswordEncoder() }
        bindEagerSingleton { PostService(instance()) }
        bindEagerSingleton { FileService(instance()) }
        bindEagerSingleton { UserService(instance(),instance(), instance())}
        bindEagerSingleton {
            FileRepositoryImp(
                uploadPath = instance(tag = "upload-dir")
            )
        }
        bindEagerSingleton {
            RoutingV1(
                staticPath = instance(tag = "upload-dir"),
                postService = instance(),
                fileService = instance(),
                userService = instance()
            )
        }
    }

    install(Authentication) {
        jwt {
            val jwtService by closestDI().instance<JWTTokenService>()
            verifier(jwtService.verifier)
            val userService by closestDI().instance<UserService>()

            validate {
                val id = it.payload.getClaim("id").asLong()
                userService.getModelById(id)
            }
        }
    }

    install(Routing) {
        val routingV1 by closestDI().instance<RoutingV1>()
        routingV1.setup(this)
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