package ru.hell.server.modelDto
import kotlinx.serialization.*

@Serializable
data class PostReceiveDto (
    var type    : String,
    var author  : String,
    var content : String? = null
)
