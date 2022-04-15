package ru.hell.server.repository

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.plugins.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.hell.server.model.MediaType
import ru.hell.server.modelDto.MediaResponseDto
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class FileRepositoryImp(private val uploadPath: String) : FileRepository {
    private val images = listOf(ContentType.Image.JPEG, ContentType.Image.PNG)

    init {
        println(Paths.get(uploadPath).toAbsolutePath().toString())
        if (Files.notExists(Paths.get(uploadPath))) {
            Files.createDirectory(Paths.get(uploadPath))
        }
    }

    override suspend fun save(multipart: MultiPartData): MediaResponseDto? {

        var response: MediaResponseDto? = null
        multipart.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {
                    if (part.name == "file") {
                        if (!images.contains(part.contentType)) {
                            throw UnsupportedMediaTypeException(part.contentType ?: ContentType.Any)
                        }
                        val ext = when (part.contentType) {
                            ContentType.Image.JPEG -> "jpg"
                            ContentType.Image.PNG -> "png"
                            else -> throw UnsupportedMediaTypeException(part.contentType!!)
                        }
                        val name = "${UUID.randomUUID()}.$ext"
                        val path = Paths.get(uploadPath, name)
                        part.streamProvider().use {
                            withContext(Dispatchers.IO) {
                                Files.copy(it, path)
                            }
                        }
                        part.dispose()
                        response = MediaResponseDto(name, MediaType.IMAGE)
                        return@forEachPart
                    }
                }
            }
            part.dispose()
        }
        return response ?: throw BadRequestException("No file field in request")
    }

    override suspend fun remove(name: String): String? {
        TODO("Not yet implemented")
    }
}