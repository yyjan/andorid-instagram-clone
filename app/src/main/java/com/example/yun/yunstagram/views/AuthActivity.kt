package com.example.yun.yunstagram.views

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.utilities.replaceFragmentInActivity
import com.example.yun.yunstagram.viewmodels.AuthViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class AuthActivity : DaggerAppCompatActivity() {

    private lateinit var loginViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_auth)

        replaceFragmentInActivity(findOrCreateViewFragment(), R.id.contentFrame)
    }

    private fun findOrCreateViewFragment() =
        supportFragmentManager.findFragmentById(R.id.contentFrame) ?: AuthLoginFragment.newInstance()

}
