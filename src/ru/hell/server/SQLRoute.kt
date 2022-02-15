package ru.hell.server

import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException


val postSql =
    """SELECT post.id, users.name AS author, postype.type, source, content, created, likeCount, sharedCount, commentCount, address, youtube, liked, ST_X(location) AS first, ST_Y(location) AS second, img, url 
         FROM post INNER JOIN (users CROSS JOIN postype) ON (users.id = post.author AND postype.id = post.type)
        ORDER BY created DESC """.trimIndent()

val commercialSql =
    """SELECT promo.id, postype.type, member.company AS author, content, img, url, sharedCount, commentCount, likeCount, weight, limitation, views AS second 
         FROM promo INNER JOIN (member CROSS JOIN postype) ON (member.id = promo.manager AND postype.id = promo.type) 
        ORDER BY weight DESC """.trimIndent()

val getPostByIdSql =
    """SELECT post.id, users.name AS author, postype.type, source, content, created, likeCount, sharedCount, commentCount, address, youtube, liked, ST_X(location) AS first, ST_Y(location) AS second, img, url 
         FROM post INNER JOIN (users CROSS JOIN postype) ON (users.id = post.author AND postype.id = post.type) 
        WHERE post.id = ? """.trimIndent()

var listPost = mutableListOf<Post>()
var i = 0
val j = 2
var y = 0


fun Routing.sql() {
    get("/post") {
        call.application.environment.log.info("-------->>> Request to API <<<--------")
        try {
            connect().use {
                val posteQuery = it.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)
                val rsultPostSql = posteQuery.executeQuery(postSql)


                val commercialQuery = it.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)
                val rsultCommercialSql = commercialQuery.executeQuery(commercialSql)

                println("Соединение установлено")
                println("###################################################################################")


                listPost.clear()
                while (rsultPostSql.next()) {
                    i = rsultPostSql.row
                    if (y % j == 0 && i != 1) {
                        rsultCommercialSql.next()
                        listPost.add(
                            Post(
                                type = rsultCommercialSql.getString("type"),
                                //id = rsultCommercialSql.getInt("id"),
                                author = rsultCommercialSql.getString("author"),
                                content = rsultCommercialSql.getString("content"),
                                //created = rsultCommercialSql.getString("created"),
                                //liked = rsultCommercialSql.getBoolean("liked"),
                                sharedCount = rsultCommercialSql.getInt("sharedCount"),
                                commentCount = rsultCommercialSql.getInt("commentCount"),
                                likeCount = rsultCommercialSql.getInt("likeCount"),
/*                              address = rsultCommercialSql.getString("address"),
                                idVideoYT = rsultCommercialSql.getString("youtube"),
                                location = Pair(
                                    rsultCommercialSql.getDouble("first"),
                                    rsultCommercialSql.getDouble("second")
                                ),
                                source = rsultCommercialSql.getInt("source"),*/
                                img = rsultCommercialSql.getString("img"),
                                url = rsultCommercialSql.getString("url")
                            )
                        )
                    }
                    listPost.add(
                        Post(
                            type = rsultPostSql.getString("type"),
                            id = rsultPostSql.getInt("id"),
                            author = rsultPostSql.getString("author"),
                            content = rsultPostSql.getString("content"),
                            created = rsultPostSql.getString("created"),
                            liked = rsultPostSql.getBoolean("liked"),
                            sharedCount = rsultPostSql.getInt("sharedCount"),
                            commentCount = rsultPostSql.getInt("commentCount"),
                            likeCount = rsultPostSql.getInt("likeCount"),
                            address = rsultPostSql.getString("address"),
                            idVideoYT = rsultPostSql.getString("youtube"),
                            location = Pair(
                                rsultPostSql.getDouble("first"),
                                rsultPostSql.getDouble("second")
                            ),
                            source = getPostById(rsultPostSql.getInt("source")),
                            img = rsultPostSql.getString("img"),
                            url = rsultPostSql.getString("url")
                        )
                    )
                    y++
                }
                rsultPostSql.close()
                rsultPostSql.close()
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

fun getPostById(postId: Int): Post? {
    lateinit var post : Post
    if (postId == 0 ){
        return null
    } else {
        try {
            connect().use {
                val ps = it.prepareStatement(getPostByIdSql)
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
    val password = "********"
    val driverNew = "com.mysql.cj.jdbc.Driver"

    Class.forName(driverNew)
    val c = DriverManager.getConnection(url, username, password)
    c.autoCommit = false
    return c
}





