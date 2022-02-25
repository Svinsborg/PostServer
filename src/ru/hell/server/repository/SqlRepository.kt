package ru.hell.server.repository

object SqlRepository {
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

    val dropPostByIdSql =
        """DELETE FROM `SocialNetwork`.`post` 
            WHERE (post.id = ?) LIMIT 1""".trimIndent()

    val getAllUserSql =
        """SELECT users.name FROM users""".trimIndent()

    val saveNewPostSql =
        """INSERT INTO SocialNetwork.post (author, type, content, created, liked, likeCount, sharedCount, commentCount) 
           VALUES (?, ?, ?, ?, ?, ?, ?, ?)""".trimIndent()

    val likeByIdSql =
        """UPDATE SocialNetwork.post
              SET liked = ?, likeCount = likeCount + 1            
            WHERE post.id = ? """.trimIndent()

    val dislikeByIdSql =
        """UPDATE SocialNetwork.post
              SET liked = ?, likeCount = likeCount - 1            
            WHERE post.id = ? """.trimIndent()
}