package com.example.yun.yunstagram.ui.search

import android.os.Bundle
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.utilities.setupActionBar
import dagger.android.support.DaggerAppCompatActivity

class SearchActivity : DaggerAppCompatActivity() {

    private var uid: String? = null

    private var searchType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setupIntent()

        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    SearchFragment.newInstance(
                        uid,
                        searchType
                    )
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
            searchType = intent.getStringExtra(EXTRA_SEARCH_TYPE)
        }
    }

    companion object {
        const val EXTRA_UID = "UID"
        const val EXTRA_SEARCH_TYPE = "SEARCH_TYPE"
    }

}
