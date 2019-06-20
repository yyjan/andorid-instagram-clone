package com.example.yun.yunstagram.data

import android.net.Uri
import com.androidhuman.rxfirebase2.firestore.model.Value
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.Completable
import io.reactivex.Single

interface DataSource {
    fun login(email: String, password: String): Single<FirebaseUser>

    fun signUp(email: String, password: String): Single<FirebaseUser>

    fun signOut(): Completable

    fun updateUser(user: User): Completable

    fun getUser(uid: String): Single<Value<DocumentSnapshot>>

    fun getCurrentUser() : FirebaseUser?

    fun getCurrentUid(): String?

    fun uploadFile(uri: Uri, callback: UploadCallback)

    fun updatePost(post: Post): Completable

    fun createPost(post: Post): Completable

    fun deletePost(id: String): Completable

    fun getPost(id: String): Single<Value<DocumentSnapshot>>

    fun getMyPosts(author: String): Single<Value<QuerySnapshot>>

    interface UploadCallback {
        fun onSuccess(downloadUri: String)
        fun onFailed(messages: String)
    }

}