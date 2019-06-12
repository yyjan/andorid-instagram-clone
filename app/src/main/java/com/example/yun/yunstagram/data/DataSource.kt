package com.example.yun.yunstagram.data

import android.net.Uri
import com.androidhuman.rxfirebase2.firestore.model.Value
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import io.reactivex.Completable
import io.reactivex.Single

interface DataSource {
    fun login(email: String, password: String): Single<FirebaseUser>

    fun signUp(email: String, password: String): Single<FirebaseUser>

    fun updateUser(user: User): Completable

    fun getUser(uid: String): Single<Value<DocumentSnapshot>>

    fun getCurrentUid(): String?

    fun uploadFile(uri: Uri, callback: UploadCallback)

    fun updatePost(post: Post): Completable

    interface UploadCallback {
        fun onSuccess(downloadUri: String)
        fun onFailed(messages: String)
    }

}