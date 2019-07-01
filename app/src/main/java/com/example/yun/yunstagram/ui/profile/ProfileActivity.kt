package com.example.yun.yunstagram.ui.profile

import android.os.Bundle
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.utilities.setupActionBar
import dagger.android.support.DaggerAppCompatActivity

class ProfileActivity : DaggerAppCompatActivity() {

    private var uid: String? = null

    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupIntent()

        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    ProfileFragment.newInstance(uid)
                )
                .commitNow()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupIntent() {
        intent?.let {
            uid = intent.getStringExtra(EXTRA_UID)
            userName = intent.getStringExtra(EXTRA_USER_NAME)
        }
    }

    companion object {
        const val EXTRA_UID = "UID"
        const val EXTRA_USER_NAME = "USER_NAME"
    }

}
