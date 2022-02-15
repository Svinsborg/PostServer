package ru.hell.server

class PostDto(
    val type: String?,
    val id: Int? = null,
    val author: String?,
    val content: String? = null,
    val created: String? = null,
    var liked: Boolean? = false,
    var sharedCount: Int?,
    var commentCount: Int?,
    var likeCount: Int?,
    val address: String? = null,
    val idVideoYT: String? = null,
    val location: Pair<Double?, Double?>? = null,
    val source: Post? = null,
    val img: String? = null,
    val url: String? = null
) {
    companion object {
        fun fromModel(model: Post) = PostDto(
            type = model.type,
            id = model.id,
            author = model.author,
            content = model.content,
            created = model.created,
            liked = model.liked,
            sharedCount = model.sharedCount,
            commentCount = model.commentCount,
            likeCount = model.likeCount,
            address = model.address,
            idVideoYT = model.idVideoYT,
            location = model.location,
            source = model.source,
            img = model.img,
            url = model.url
        )
    }
}