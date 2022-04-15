package ru.hell.server.modelDto

import kotlinx.serialization.Serializable

@Serializable
class UserModelReceiveDto(
    val id: Long = 0,
    val username: String,
    val email: String? = null,
    val registry: Long? = null,
    val banned: Boolean = false,
    val avatar: String? = null
) {
}