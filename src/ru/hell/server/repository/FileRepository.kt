package ru.hell.server.repository

import io.ktor.http.content.*
import ru.hell.server.modelDto.MediaResponseDto

interface FileRepository {
    suspend fun save(multipart: MultiPartData): MediaResponseDto?
    suspend fun remove(name: String) :String?
}