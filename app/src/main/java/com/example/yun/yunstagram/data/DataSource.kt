package com.example.yun.yunstagram.data

import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single

interface DataSource {
    fun login(email: String, password: String): Single<FirebaseUser>

    fun signUp(email: String, password: String): Single<FirebaseUser>

    fun updateUser(user: User) : Completable

}