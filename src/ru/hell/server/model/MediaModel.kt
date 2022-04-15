package ru.hell.server.model
enum class MediaType {
    IMAGE
}
class MediaModel(
    val id: String,
    val mediaType: MediaType
    ) {}