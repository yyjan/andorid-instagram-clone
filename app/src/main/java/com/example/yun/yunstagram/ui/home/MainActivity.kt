package com.example.yun.yunstagram.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.data.DataRepository
import com.example.yun.yunstagram.ui.favorite.FavoriteFragment
import com.example.yun.yunstagram.utilities.replaceFragmentInActivity
import com.example.yun.yunstagram.ui.post.PostEditActivity
import com.example.yun.yunstagram.ui.profile.ProfileFragment
import com.example.yun.yunstagram.ui.search.SearchFragment
import com.example.yun.yunstagram.ui.search.SearchListType
import com.example.yun.yunstagram.utilities.Constants.REQUEST_CODE_FOR_POST_EDIT
import com.example.yun.yunstagram.utilities.setupActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.layout_app_bar.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var repository: DataRepository

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                replaceFragment(HomeFragment.newInstance(), "HomeFragment")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                replaceFragment(SearchFragment.newInstance(type = SearchListType.USERS.name), "SearchFragment")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_control -> {
                openActivity(Intent(this, PostEditActivity::class.java))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorite -> {
                replaceFragment(FavoriteFragment.newInstance(), "FavoriteFragment")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {
                replaceFragment(ProfileFragment.newInstance(repository.getCurrentUid()), "ProfileFragment")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navView.itemIconTintList = null
        replaceFragment(HomeFragment.newInstance(), "HomeFragment")

        setupActionBar(R.id.toolbar) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.findFragmentByTag("ProfileFragment")
        fragment?.onActivityResult(requestCode, resultCode, intent)
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        replaceLogo(fragment)
        replaceFragmentInActivity(fragment, R.id.contentFrame, tag)
    }

    private fun replaceLogo(fragment: Fragment) {
        toolbar?.run {
            when (fragment) {
                is HomeFragment -> setLogo(R.drawable.ic_appbar_logo)
                else -> logo = null
            }
        }
    }

    private fun openActivity(intent: Intent) {
        startActivityForResult(Intent(this, PostEditActivity::class.java), REQUEST_CODE_FOR_POST_EDIT)
    }
}
