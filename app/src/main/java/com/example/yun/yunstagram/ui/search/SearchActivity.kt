package com.example.yun.yunstagram.ui.search

import android.os.Bundle
import com.example.yun.yunstagram.R
import dagger.android.support.DaggerAppCompatActivity

class SearchActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container,
                    SearchFragment.newInstance())
                .commitNow()
        }
    }
}
