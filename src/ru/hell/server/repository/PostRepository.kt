package ru.hell.server.repository

import ru.hell.server.model.Post

interface PostRepository {
    suspend fun getAll(): List<Post>
    suspend fun getById(id: Long): Post?
    suspend fun save(item: Post): Post
    suspend fun removeById(id: Long) :String
    suspend fun likeById(id: Long): Post?
    suspend fun dislikeById(id: Long): Post?
}