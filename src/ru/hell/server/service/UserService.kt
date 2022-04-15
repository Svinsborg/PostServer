package ru.hell.server.service

import io.ktor.server.plugins.*
import org.springframework.security.crypto.password.PasswordEncoder
import ru.hell.server.exception.InvalidPasswordException
import ru.hell.server.exception.PasswordChangeException
import ru.hell.server.model.UserModel
import ru.hell.server.modelDto.*
import ru.hell.server.repository.UserRepository


class UserService (
    private val repo: UserRepository,
    private val tokenService: JWTTokenService,
    private val passwordEncoder: PasswordEncoder
) {
    suspend fun getModelById(id: Long): UserModel? {
        return repo.getById(id)
    }

    suspend fun getById(id: Long): UserResponseDto {
        val model = repo.getById(id) ?: throw NotFoundException()
        return UserResponseDto.fromModel(model)
    }

    suspend fun changePassword(id: Long, input: PasswordChangeRequestDto) {
        // TODO: handle concurrency
        val model = repo.getById(id) ?: throw NotFoundException()
        if (!passwordEncoder.matches(input.old, model.password)) {
            throw PasswordChangeException("Wrong password!")
        }
        val copy = model.copy(password = passwordEncoder.encode(input.new))
        repo.save(copy)
    }

    suspend fun authenticate(input: AuthenticationRequestDto): AuthenticationResponseDto {
        val model = repo.getByUsername(input.username) ?: throw NotFoundException()
        if (!passwordEncoder.matches(input.password, model.password)) {
            throw InvalidPasswordException("Wrong password!")
        }

        val token = tokenService.generate(model.id)
        return AuthenticationResponseDto(token)
    }

    suspend fun save(item: UserModel) : UserModelDto? {
        val model = UserModel(
            username = item.username,
            password = passwordEncoder.encode(item.password)
        )
        return UserModelDto.fromModel(repo.save(model) ?: throw NotFoundException())
    }

}