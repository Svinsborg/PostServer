package ru.hell.server.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import ru.hell.server.model.Post
import ru.hell.server.modelDto.PostDto
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import kotlin.random.Random

private var listPost = mutableListOf<Post>()
private val query = SqlRepository

@OptIn(ExperimentalCoroutinesApi::class)
private val context = newSingleThreadContext("PostRepositorySingleThreaded")

class PostRepositoryImp : PostRepository {

    override suspend fun getAll(): List<Post> =
        withContext(context) {
            try {
                connect().use {
                    val posteQuery = it.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)
                    val commercialQuery =
                        it.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)

                    var i = 0
                    val j = 2
                    var y = 0

                    posteQuery.executeQuery(query.postSql).use { resultPostSql ->
                        commercialQuery.executeQuery(query.commercialSql).use { resultCommercialSql ->

                            println("Соединение установлено")
                            println("###################################################################################")

                            listPost.clear()
                            while (resultPostSql.next()) {
                                i = resultPostSql.row
                                if (y % j == 0 && i != 1) {

                                    resultCommercialSql.next()
                                    if (resultCommercialSql.isLast) {
                                        resultCommercialSql.first()
                                        addCommercialPost(resultCommercialSql)
                                    } else {
                                        addCommercialPost(resultCommercialSql)
                                    }
                                }

                                listPost.add(
                                    Post(
                                        type = resultPostSql.getString("type"),
                                        id = resultPostSql.getInt("id"),
                                        author = resultPostSql.getString("author"),
                                        content = resultPostSql.getString("content"),
                                        created = resultPostSql.getString("created"),
                                        liked = resultPostSql.getBoolean("liked"),
                                        sharedCount = resultPostSql.getInt("sharedCount"),
                                        commentCount = resultPostSql.getInt("commentCount"),
                                        likeCount = resultPostSql.getInt("likeCount"),
                                        address = resultPostSql.getString("address"),
                                        idVideoYT = resultPostSql.getString("youtube"),
                                        location = Pair(
                                            resultPostSql.getDouble("first"),
                                            resultPostSql.getDouble("second")
                                        ),
                                        source = getById(resultPostSql.getLong("source")),
                                        img = resultPostSql.getString("img"),
                                        url = resultPostSql.getString("url")
                                    )
                                )
                                y++
                            }
                            println("Количество записей = $i")
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
            return@withContext listPost
        }

    override suspend fun getById(id: Long): Post? =
        withContext(context) {
            var post: Post? = null
            if (id.equals(0L)) {
                return@withContext null
            } else {
                try {
                    connect().use {
                        val ps = it.prepareStatement(query.getPostByIdSql)
                        ps.setLong(1, id)
                        ps.executeQuery().use { postByIdQuery ->
                            println("Соединение установлено")
                            if (postByIdQuery != null) {
                                postByIdQuery.next()
                                post = Post(
                                    type = postByIdQuery.getString("type"),
                                    id = postByIdQuery.getInt("id"),
                                    author = postByIdQuery.getString("author"),
                                    content = postByIdQuery.getString("content"),
                                    created = postByIdQuery.getString("created"),
                                    liked = postByIdQuery.getBoolean("liked"),
                                    sharedCount = postByIdQuery.getInt("sharedCount"),
                                    commentCount = postByIdQuery.getInt("commentCount"),
                                    likeCount = postByIdQuery.getInt("likeCount"),
                                    address = postByIdQuery.getString("address"),
                                    idVideoYT = postByIdQuery.getString("youtube"),
                                    location = Pair(
                                        postByIdQuery.getDouble("first"),
                                        postByIdQuery.getDouble("second")
                                    ),
                                    img = postByIdQuery.getString("img"),
                                    url = postByIdQuery.getString("url")
                                )
                                println(post)
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
            }
            return@withContext post
        }

    override suspend fun save(item: Post): Post? =
        withContext(context) {
            if (item.type < 5.toString()) {
                try {
                    connect().use {
                        val moscowGMT = 10800L // +3 GMT
                        val author = item.author
                        val type = item.type
                        val content = item.content
                        val created = (System.currentTimeMillis() / 1000 + moscowGMT).toString()
                        val liked = Random.nextInt(0, 1).toString()
                        val likeCount = Random.nextInt(0, 50).toString()
                        val sharedCount = Random.nextInt(0, 50).toString()
                        val commentCount = Random.nextInt(0, 50).toString()
                        val ps = it.prepareStatement(query.saveNewPostSql)
                        ps.setString(1, author)
                        ps.setString(2, type)
                        ps.setString(3, content)
                        ps.setString(4, created)
                        ps.setString(5, liked)
                        ps.setString(6, likeCount)
                        ps.setString(7, sharedCount)
                        ps.setString(8, commentCount)
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
            } else {
                return@withContext (null)
            }
            return@withContext (item)
        }

    override suspend fun removeById(id: Long): String =

        withContext(context) {
            if (id > 8) {
                try {
                    connect().use {
                        val ps = it.prepareStatement(query.dropPostByIdSql)
                        ps.setLong(1, id)
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
                    return@withContext ("Отказ")
                }
            } else {
                return@withContext ("Запрет!")
            }
            return@withContext ("Успех")
        }

    override suspend fun likeById(id: Long): Post? =
        withContext(context) {
            try {
                connect().use {
                    val ps = it.prepareStatement(query.likeByIdSql)
                    ps.setBoolean(1, true)
                    ps.setLong(2, id)
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
                return@withContext (null)
            }
            return@withContext getById(id)
        }

    override suspend fun dislikeById(id: Long): Post? =
        withContext(context) {
            try {
                connect().use {
                    val ps = it.prepareStatement(query.dislikeByIdSql)
                    ps.setBoolean(1, false)
                    ps.setLong(2, id)
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
                return@withContext (null)
            }
            return@withContext getById(id)
        }
}

fun connect(): Connection {
    val url = "jdbc:mysql://192.168.1.78:3306/SocialNetwork?serverTimezone=UTC"
    val username = "post"
    val password = "********"
    val driverNew = "com.mysql.cj.jdbc.Driver"
    Class.forName(driverNew)
    val c = DriverManager.getConnection(url, username, password)
    c.autoCommit = true
    return c
}

fun addCommercialPost(resultCommercialSql: ResultSet) {
    listPost.add(
        Post(
            type = resultCommercialSql.getString("type"),
            author = resultCommercialSql.getString("author"),
            content = resultCommercialSql.getString("content"),
            sharedCount = resultCommercialSql.getInt("sharedCount"),
            commentCount = resultCommercialSql.getInt("commentCount"),
            likeCount = resultCommercialSql.getInt("likeCount"),
            img = resultCommercialSql.getString("img"),
            url = resultCommercialSql.getString("url")
        )
    )
}