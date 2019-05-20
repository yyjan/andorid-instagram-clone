package com.example.yun.yunstagram.data

data class User(
    var uid: String? = null,
    var email: String? = null,
    var username: String? = null,
    var biography: String? = null,
    var media_count: Int = 0,
    var followers_count: Int = 0,
    var follows_count: Int = 0,
    var profile_picture_url: String? = null,
    var website: String? = null
) {

}
