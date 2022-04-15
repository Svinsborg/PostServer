package ru.hell.server.service

import io.ktor.features.*
import io.ktor.http.content.*
import ru.hell.server.modelDto.MediaResponseDto
import ru.hell.server.repository.FileRepository


class FileService(private val repo: FileRepository) {
    suspend fun save(multipart: MultiPartData): MediaResponseDto {
        var response = repo.save(multipart)
        return response ?: throw NotFoundException()
    }
}