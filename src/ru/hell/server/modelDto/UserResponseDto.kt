package ru.hell.server.modelDto

import ru.hell.server.model.UserModel

class UserResponseDto (val id: Long, val username: String) {
    companion object {
        fun fromModel(model: UserModel) = UserResponseDto(
            id = model.id,
            username = model.username
        )
    }
}