package com.example.yun.yunstagram.data

import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single

interface DataSource {
    fun login(email: String, password: String): Single<FirebaseUser>

}