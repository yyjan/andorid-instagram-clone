package com.example.yun.yunstagram.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.utilities.replaceFragmentInActivity
import com.example.yun.yunstagram.ui.post.PostEditActivity
import com.example.yun.yunstagram.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                replaceFragment(HomeFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_control -> {
                openActivity(Intent(this, PostEditActivity::class.java))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorite -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {
                replaceFragment(ProfileFragment.newInstance())
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
        replaceFragment(HomeFragment.newInstance())
    }

    private fun replaceFragment(fragment: Fragment) {
        replaceFragmentInActivity(fragment, R.id.contentFrame)
    }

    private fun openActivity(intent: Intent){
        startActivity(Intent(this, PostEditActivity::class.java))
    }
}
