package com.example.yun.yunstagram.utilities

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int) {
    supportFragmentManager.transact {
        replace(frameId, fragment)
    }
}

fun addFragment(activity: FragmentActivity, fragment: Fragment, frameId: Int, fragmentTag: String) {
    activity.supportFragmentManager?.transact {
        addToBackStack(fragmentTag)
        replace(frameId, fragment, fragmentTag)
    }
}

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commitAllowingStateLoss()
}