package com.example.yun.yunstagram.views

import android.os.Bundle
import com.example.yun.yunstagram.R
import dagger.android.support.DaggerAppCompatActivity

class ProfileEditActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProfileEditFragment.newInstance())
                .commitNow()
        }
    }

}
