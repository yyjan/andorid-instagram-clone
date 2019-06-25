package com.example.yun.yunstagram.ui.profile

import android.os.Bundle
import com.example.yun.yunstagram.R
import dagger.android.support.DaggerAppCompatActivity

class ProfileActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container,
                    ProfileFragment.newInstance(intent.getStringExtra(EXTRA_UID)))
                .commitNow()
        }
    }

    companion object {
        const val EXTRA_UID = "UID"
    }

}
