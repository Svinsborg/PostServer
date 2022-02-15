package ru.hell.server

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.root(){
    get("/"){
        call.respondText("Hello, im Ktor!", ContentType.Text.Plain)
    }
}

fun Routing.rootPost(){
    post("/"){
        val post = call.receive<String>()
        call.respond("Recived $post from the post body")
    }
}