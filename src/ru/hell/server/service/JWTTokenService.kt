package ru.hell.server.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JWTTokenService {
    private val secret = "**************"
    private val algo = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier = JWT.require(algo).build()

    fun generate(id: Long): String = JWT.create()
        .withClaim("id", id)
        // TODO: Comment this to disable expiration
        .withExpiresAt(Date(System.currentTimeMillis() + 100000))
        .sign(algo)
}