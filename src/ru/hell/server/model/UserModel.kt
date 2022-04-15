package ru.hell.server.model

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class UserModel (
    val id: Long = 0,
    val username: String,
    val password: String,
    val email: String? = null,
    val registry: Long? = null,
    val banned: Boolean = false,
    val avatar: String? = null
): Principal