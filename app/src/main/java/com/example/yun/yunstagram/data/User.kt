package com.example.yun.yunstagram.data

data class User(
    val id: String,
    val username: String,
    val biography: String,
    val media_count: Int,
    val followers_count: Int,
    val follows_count: Int,
    val profile_picture_url: String,
    val website: String
) {

}
