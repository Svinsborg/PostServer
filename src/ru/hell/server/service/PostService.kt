package ru.hell.server.service

import io.ktor.server.plugins.*
import ru.hell.server.model.Post
import ru.hell.server.modelDto.PostDto
import ru.hell.server.modelDto.PostReceiveDto
import ru.hell.server.repository.PostRepository


class PostService(private val repo: PostRepository) {
    suspend fun getAll(): List<PostDto> {
        return repo.getAll().map { PostDto.fromModel(it)}
    }

    suspend fun getById(id: Long): PostDto?{
        val model = repo.getById(id) ?: throw NotFoundException()
        return PostDto.fromModel(model)
    }

    suspend fun save(item: PostReceiveDto): PostDto?{
        val model = Post(
            type = item.type,
            author = item.author,
            content = item.content
        )
        return PostDto.fromModel (repo.save(model) ?: throw NotFoundException())
    }

    suspend fun removeById(id: Long) :String{
        return repo.removeById(id)
    }

    suspend fun likeById(id: Long): Post?{
        return repo.likeById(id)
    }

    suspend fun dislikeById(id: Long): Post?{
        return repo.dislikeById(id)
    }
}