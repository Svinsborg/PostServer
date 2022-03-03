package ru.hell.server.route

import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.*
import ru.hell.server.model.Post
import ru.hell.server.modelDto.PostReceiveDto
import ru.hell.server.repository.PostRepository
import ru.hell.server.repository.PostRepositoryImp

val di = DI {
    bindSingleton<PostRepository> { PostRepositoryImp() }
}

fun Routing.v1() {
    install(ContentNegotiation) {
        gson()                  // Gson converter
    }

    route("/api/v1/post") {
        val repo by di.instance<PostRepository>()
        get {
            call.application.environment.log.info("\n-------->>> Request to API <<<--------\n")
            val response = repo.getAll()
            call.respond(response)
            response.forEach(System.out::println)
            call.application.environment.log.info("\n-------->>> Response to API: ALL POST<<<--------\n $response")
        }
        get("/{id}") {
            call.application.environment.log.info("\n-------->>> Request to API <<<--------\n")
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val post = repo.getById(id)
            val json = (Gson().toJson(post))
            call.application.environment.log.info("\n-------->>> Response to API: POST BY ID <<<--------\n $json")
            call.respond(json)
        }

        post {
            val request = call.receive<PostReceiveDto>()
            val model = Post(
                type = request.type,
                author = request.author,
                content = request.content
            )
            val response = repo.save(model)
            call.respond(Gson().toJson(response))
        }

        post("/{id}/like") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw NotFoundException()
            val post = repo.likeById(id)
            call.respond(Gson().toJson(post))
        }

        delete("/{id}/like") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw NotFoundException()
            val post = repo.dislikeById(id)
            call.respond(Gson().toJson(post))
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw NotFoundException()
            val post = repo.removeById(id)
            call.respond(post)
        }
    }
}



