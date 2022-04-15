package ru.hell.server.modelDto

import ru.hell.server.model.UserModel

class UserModelDto(
    val id: Long = 0,
    val username: String,
    val password: String,
    val email: String?,
    val registry: Long?,
    val banned: Boolean,
    val avatar: String? = null
) {
    companion object {
        fun fromModel(model: UserModel) = UserModelDto(
            id = model.id,
            username = model.username,
            password = model.password,
            email = model.email,
            registry = model.registry,
            banned = model.banned,
            avatar = model.avatar
        )
    }
}