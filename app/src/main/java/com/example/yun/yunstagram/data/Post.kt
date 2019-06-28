package com.example.yun.yunstagram.data

data class Post(
    var id: String? = null,
    var created_time: String? = null,
    var updated_time: String? = null,
    var message: String? = null,
    var isHidden: Boolean = false,
    var like_count: Int? = 0,
    var username: String? = null,
    var author: String? = null,
    var picture_url: String? = null,
    var user : User? = User()
)

// comments
// attachments