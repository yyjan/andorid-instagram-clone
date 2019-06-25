package com.example.yun.yunstagram.data

data class User(
    var uid: String? = null,
    var email: String? = null,
    var username: String? = null,
    var biography: String? = null,
    var website: String? = null,
    var media_count: Int? = 0,
    var followers: List<String>? = null,
    var following: List<String>? = null,
    var profile_picture_url: String? = null
) {

}
