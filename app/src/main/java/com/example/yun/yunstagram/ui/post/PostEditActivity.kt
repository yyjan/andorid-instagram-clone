package com.example.yun.yunstagram.ui.post

import android.os.Bundle
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.utilities.setupActionBar
import dagger.android.support.DaggerAppCompatActivity

class PostEditActivity : DaggerAppCompatActivity() {

    private var postId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_edit)

        postId = intent.getStringExtra(EXTRA_POST_ID)

        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)

            title = if (postId == null) {
                getString(R.string.title_post_create)
            } else {
                getString(R.string.title_post_edit)
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PostEditFragment.newInstance(intent.getStringExtra(EXTRA_POST_ID)))
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
