package com.example.yun.yunstagram.data

import com.androidhuman.rxfirebase2.auth.RxFirebaseAuth
import com.androidhuman.rxfirebase2.firestore.RxFirebaseFirestore
import com.androidhuman.rxfirebase2.firestore.model.Value
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single

class DataRepository() : DataSource {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var loggedInUser: FirebaseUser? = null

    override fun login(email: String, password: String): Single<FirebaseUser> {
        return RxFirebaseAuth.signInWithEmailAndPassword(auth, email, password)
    }

    override fun signUp(email: String, password: String): Single<FirebaseUser> {
        return RxFirebaseAuth.createUserWithEmailAndPassword(auth, email, password)
    }

    override fun updateUser(user: User): Completable {
        return RxFirebaseFirestore.set(db.collection("user").document(user.uid!!), user)
    }

    override fun getUser(uid: String): Single<Value<DocumentSnapshot>> {
        return RxFirebaseFirestore.data(db.collection("user").document(uid))
    }

    override fun getCurrentUid(): String? {
        return auth.currentUser?.uid
    }

    private fun setLoggedInUser(loggedInUser: FirebaseUser?) {
        this.loggedInUser = loggedInUser
    }

}