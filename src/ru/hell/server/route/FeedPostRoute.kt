package ru.hell.server.route

import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.hell.server.model.Post
import ru.hell.server.repository.SqlRepository
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

var listPost = mutableListOf<Post>()
val query = SqlRepository

fun Routing.getAllPost() {
    get("/api/v1/post") {
        call.application.environment.log.info("\n-------->>> Request to API <<<--------\n")
        try {
            connect().use {
                val posteQuery = it.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)
                val resultPostSql = posteQuery.executeQuery(query.postSql)

                val commercialQuery = it.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)
                val resultCommercialSql = commercialQuery.executeQuery(query.commercialSql)

                var i = 0
                val j = 2
                var y = 0

                println("Соединение установлено")
                println("###################################################################################")

                listPost.clear()
                while (resultPostSql.next()) {
                    i = resultPostSql.row
                    if (y % j == 0 && i != 1) {
                        resultCommercialSql.next()
                        if (resultCommercialSql.isLast){
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
                            source = getPostById(resultPostSql.getInt("source")),
                            img = resultPostSql.getString("img"),
                            url = resultPostSql.getString("url")
                        )
                    )
                    y++
                }
                resultPostSql.close()
                resultPostSql.close()
                connect().close()
                val json = (Gson().toJson(listPost))
                listPost.forEach(System.out::println)
                println("Количество записей = $i")
                println(json)
                call.respond(json)
                call.application.environment.log.info("\n-------->>> Response to API: <<<--------\n $json")
            }

        } catch (err: SQLException) {
            err.printStackTrace()
            println("Не удалось подключиться")
            println("SQLState: " + err.getSQLState())
            println("VendorError: " + err.getErrorCode())
            println("Others: " + err.message)
        }
        connect().close()
    }
}

fun addCommercialPost(resultCommercialSql: ResultSet) {
    listPost.add(
        Post(
            type = resultCommercialSql.getString("type"),
            //id = resultCommercialSql.getInt("id"),
            author = resultCommercialSql.getString("author"),
            content = resultCommercialSql.getString("content"),
            //created = resultCommercialSql.getString("created"),
            //liked = resultCommercialSql.getBoolean("liked"),
            sharedCount = resultCommercialSql.getInt("sharedCount"),
            commentCount = resultCommercialSql.getInt("commentCount"),
            likeCount = resultCommercialSql.getInt("likeCount"),
/*                              address = resultCommercialSql.getString("address"),
                                idVideoYT = resultCommercialSql.getString("youtube"),
                                location = Pair(
                                    resultCommercialSql.getDouble("first"),
                                    resultCommercialSql.getDouble("second")
                                ),
                                source = resultCommercialSql.getInt("source"),*/
            img = resultCommercialSql.getString("img"),
            url = resultCommercialSql.getString("url")
        )
    )
}


fun getPostById(postId: Int): Post? {
    lateinit var post : Post
    if (postId == 0 ){
        return null
    } else {
        try {
            connect().use {
                val ps = it.prepareStatement(query.getPostByIdSql)
                ps.setInt(1, postId)
                val posteByIdQuery = ps.executeQuery()
                println("Соединение установлено")
                if (posteByIdQuery != null) {
                    posteByIdQuery.next()
                    post = Post(
                        type = posteByIdQuery.getString("type"),
                        id = posteByIdQuery.getInt("id"),
                        author = posteByIdQuery.getString("author"),
                        content = posteByIdQuery.getString("content"),
                        created = posteByIdQuery.getString("created"),
                        liked = posteByIdQuery.getBoolean("liked"),
                        sharedCount = posteByIdQuery.getInt("sharedCount"),
                        commentCount = posteByIdQuery.getInt("commentCount"),
                        likeCount = posteByIdQuery.getInt("likeCount"),
                        address = posteByIdQuery.getString("address"),
                        idVideoYT = posteByIdQuery.getString("youtube"),
                        location = Pair(
                            posteByIdQuery.getDouble("first"),
                            posteByIdQuery.getDouble("second")
                        ),
                        img = posteByIdQuery.getString("img"),
                        url = posteByIdQuery.getString("url")
                    )
                    println(post)

                } else {
                    println("Data empty")
                    return null
                }
                posteByIdQuery.close()
            }
            connect().close()
        } catch (err: SQLException) {
            err.printStackTrace()
            println("Не удалось подключиться")
            println("SQLState: " + err.getSQLState())
            println("VendorError: " + err.getErrorCode())
            println("Others: " + err.message)
        }
        connect().close()
    }
    return post
}

fun connect(): Connection {
    val url = "jdbc:mysql://192.168.1.78:3306/SocialNetwork?serverTimezone=UTC"
    val username = "post"
    val password = "!QAZ@WSX"
    val driverNew = "com.mysql.cj.jdbc.Driver"

    Class.forName(driverNew)
    val c = DriverManager.getConnection(url, username, password)
    c.autoCommit = false
    return c
}





