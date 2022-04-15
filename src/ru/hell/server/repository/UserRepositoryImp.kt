package ru.hell.server.repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.hell.server.model.UserModel
import java.sql.SQLException

private val query = SqlRepository

class UserRepositoryImp  : UserRepository {
    private var nextId = 1L
    private val items = mutableListOf<UserModel>()
    private val mutex = Mutex()

    override suspend fun getAll(): List<UserModel> {
        mutex.withLock {
            return items.toList()
        }
    }

    override suspend fun getById(id: Long): UserModel? {
        mutex.withLock {
            return items.find { it.id == id }
        }
    }

    override suspend fun getByIds(ids: Collection<Long>): List<UserModel> {
        mutex.withLock {
            return items.filter { ids.contains(it.id) }
        }
    }

    override suspend fun getByUsername(username: String): UserModel? {
        mutex.withLock {
            return items.find { it.username == username }
        }
    }

    override suspend fun save(item: UserModel): UserModel? {
        mutex.withLock {
            val response: UserModel
            val moscowGMT = 10800L // +3 GMT
            val username = item.username
            val password = item.password
            val email = "$username@testMail.temp"
            val registry = (System.currentTimeMillis() / 1000 + moscowGMT).toString()

            //TODO реализовать проверку существующего пользователя и email
            try {
                connect().use {
                    val ps = it.prepareStatement(query.userAdd)
                    ps.setString(1, username)
                    ps.setString(2, email)
                    ps.setString(3, registry)
                    ps.setString(4, password)
                    val result = ps.executeUpdate()
                    println("Соединение установлено")
                    println(result)
                    ps.close()
                }
            } catch (err: SQLException) {
                err.printStackTrace()
                println("Не удалось подключиться")
                println("SQLState: " + err.getSQLState())
                println("VendorError: " + err.getErrorCode())
                println("Others: " + err.message)
            }
            try {
                connect().use {
                    val ps = it.prepareStatement(query.searchUserByName)
                    ps.setString(1, username)
                    ps.executeQuery().use { userTake ->
                        println("Соединение установлено")
                        if (userTake != null){
                            userTake.next()
                            response = UserModel(
                                id = userTake.getLong("id"),
                                username = userTake.getString("username"),
                                email = userTake.getString("email"),
                                registry = userTake.getLong("registry"),
                                banned = userTake.getBoolean("banned"),
                                avatar = userTake.getString("avatar"),
                                password = "********"
                            )
                            println(response)
                            return response
                        } else {
                            println("Data empty")
                            return@use null
                        }
                    }
                }
            } catch (err: SQLException) {
                err.printStackTrace()
                println("Не удалось подключиться")
                println("SQLState: " + err.getSQLState())
                println("VendorError: " + err.getErrorCode())
                println("Others: " + err.message)
            }
            return null
        }
    }
}

