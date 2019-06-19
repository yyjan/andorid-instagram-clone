package com.example.yun.yunstagram.ui.post

import android.os.Bundle
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.utilities.setupActionBar
import dagger.android.support.DaggerAppCompatActivity

class PostDetailActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PostDetailFragment.newInstance(intent.getStringExtra(EXTRA_POST_ID)))
                .commitNow()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_POST_ID = "POST_ID"
    }

}
