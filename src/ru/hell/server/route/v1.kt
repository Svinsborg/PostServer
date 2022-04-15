package ru.hell.server.route

import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.hell.server.model.UserModel
import ru.hell.server.modelDto.AuthenticationRequestDto
import ru.hell.server.modelDto.PostReceiveDto
import ru.hell.server.service.FileService
import ru.hell.server.service.PostService
import ru.hell.server.service.UserService

class RoutingV1(
    private val staticPath: String,
    private val postService: PostService,
    private val fileService: FileService,
    private val userService: UserService
) {
    fun setup(configuration: Routing) {
        with(configuration) {
            install(ContentNegotiation) {
                gson()
            }

            route("/api/v1/post") {

                get {
                    call.application.environment.log.info("\n-------->>> Request to API <<<--------\n")
                    val response = postService.getAll()
                    call.respond(response)
                    response.forEach(System.out::println)
                    call.application.environment.log.info("\n-------->>> Response to API: ALL POST<<<--------\n $response")
                }
                get("/{id}") {
                    call.application.environment.log.info("\n-------->>> Request to API <<<--------\n")
                    val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
                    val post = postService.getById(id)
                    post?.let { call.respond(it) } ?: call.respond(HttpStatusCode.NotFound)
                    call.application.environment.log.info("\n-------->>> Response to API: POST BY ID <<<--------\n $post")
                }

                post {
                    val request = call.receive<PostReceiveDto>()
                    val response = postService.save(request)
                    call.respond(Gson().toJson(response))
                }

                post("/{id}/like") {
                    val id = call.parameters["id"]?.toLongOrNull() ?: throw NotFoundException()
                    val post = postService.likeById(id)
                    call.respond(Gson().toJson(post))
                }

                delete("/{id}/like") {
                    val id = call.parameters["id"]?.toLongOrNull() ?: throw NotFoundException()
                    val post = postService.dislikeById(id)
                    call.respond(Gson().toJson(post))
                }

                delete("/{id}") {
                    val id = call.parameters["id"]?.toLongOrNull() ?: throw NotFoundException()
                    val post = postService.removeById(id)
                    call.respond(post)
                }
            }

            route("/api/v1") {

                static("/static") {
                    files(staticPath)
                }

                post("/media") {
                    val response = fileService.save(call.receiveMultipart())
                    call.respond(response)
                }

                post("/registration") {
                    val request = call.receive<UserModel>()
                    val response = userService.save(request)
                    call.respond(Gson().toJson(response))
                }

                post("/authentication") {
                    val input = call.receive<AuthenticationRequestDto>()
                    val response = userService.authenticate(input)
                    call.respond(response)
                }
            }
        }
    }
}



