package ru.hell.server.modelDto

import ru.hell.server.model.MediaModel
import ru.hell.server.model.MediaType

class MediaResponseDto(
    val id: String,
    val mediaType: MediaType
){
    companion object {
        fun fromModel(model: MediaModel) = MediaResponseDto(
            id = model.id,
            mediaType = model.mediaType
        )
    }
}