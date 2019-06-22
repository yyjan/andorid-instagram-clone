package com.example.yun.yunstagram.data

import android.net.Uri
import com.androidhuman.rxfirebase2.auth.RxFirebaseAuth
import com.androidhuman.rxfirebase2.firestore.RxFirebaseFirestore
import com.androidhuman.rxfirebase2.firestore.model.Value
import com.example.yun.yunstagram.utilities.Constants
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.reactivex.Completable
import io.reactivex.Single


class DataRepository : DataSource {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    private val usersCollection = db.collection(Constants.DB_ROOT_USERS)
    private val postsCollection = db.collection(Constants.DB_ROOT_POSTS)

    private var loggedInUser: FirebaseUser? = null

    var lastStorageRef: StorageReference? = null

    override fun login(email: String, password: String): Single<FirebaseUser> {
        return RxFirebaseAuth.signInWithEmailAndPassword(auth, email, password)
    }

    override fun signUp(email: String, password: String): Single<FirebaseUser> {
        return RxFirebaseAuth.createUserWithEmailAndPassword(auth, email, password)
    }

    override fun signOut(): Completable {
        return RxFirebaseAuth.signOut(auth)
    }

    override fun updateUser(user: User): Completable {
        return RxFirebaseFirestore.set(usersCollection.document(user.uid!!), user)
    }

    override fun getUser(uid: String): Single<Value<DocumentSnapshot>> {
        return RxFirebaseFirestore.data(usersCollection.document(uid))
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun getCurrentUid(): String? {
        return auth.currentUser?.uid
    }

    override fun updatePost(post: Post): Completable {
        return RxFirebaseFirestore.set(postsCollection.document(post.id!!), post)
    }

    override fun updatePostValue(id: String, value: Map<String, Any?>): Completable {
        return RxFirebaseFirestore.update(postsCollection.document(id), value)
    }

    override fun createPost(post: Post): Completable {
        return RxFirebaseFirestore.set(postsCollection.document(), post)
    }

    override fun deletePost(id: String): Completable {
        return RxFirebaseFirestore.delete(postsCollection.document(id))
    }

    override fun getPost(id: String): Single<Value<DocumentSnapshot>> {
        return RxFirebaseFirestore.data(postsCollection.document(id))
    }

    override fun getMyPosts(author: String): Single<Value<QuerySnapshot>> {
        return RxFirebaseFirestore.data(
            postsCollection.whereEqualTo("author", author)
        )
    }

    private fun setLoggedInUser(loggedInUser: FirebaseUser?) {
        this.loggedInUser = loggedInUser
    }

    override fun uploadFile(uri: Uri, callback: DataSource.UploadCallback) {
        val storageRef = storage.reference
        lastStorageRef = storageRef.child("images/${uri.lastPathSegment}")

        val uploadTask = lastStorageRef?.putFile(uri)
        uploadTask?.addOnFailureListener {
            callback.onFailed(it.message.toString())
        }?.addOnSuccessListener {

            // get downloadUri
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation lastStorageRef?.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    callback.onSuccess(downloadUri.toString())
                } else {
                }
            }
        }
    }
}