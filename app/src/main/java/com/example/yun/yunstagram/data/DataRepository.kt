package com.example.yun.yunstagram.data

import com.androidhuman.rxfirebase2.auth.RxFirebaseAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single

class DataRepository() : DataSource {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var loggedInUser: FirebaseUser? = null

    override fun login(email: String, password: String): Single<FirebaseUser> {
        return RxFirebaseAuth.signInWithEmailAndPassword(auth, email, password)
    }

    private fun setLoggedInUser(loggedInUser: FirebaseUser?) {
        this.loggedInUser = loggedInUser
    }

}