package ru.hell.server.model

import kotlinx.serialization.*

@Serializable
data class Post(
        val type: String,
        val id: Int? = null,
        val author: String,
        val content: String? = null,
        val created: String? = null,
        var liked: Boolean? = false,
        val sharedCount: Int = 0,
        val commentCount: Int = 0,
        val likeCount: Int = 0,
        val address: String? = null,
        val idVideoYT: String? = null,
        val location: Pair<Double?, Double?>? = null,
        val source: Post? = null,
        val img: String? = null,
        val url: String? = null
        )