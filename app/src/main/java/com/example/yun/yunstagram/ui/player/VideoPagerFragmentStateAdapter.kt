package com.example.yun.yunstagram.ui.player

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.yun.yunstagram.data.User

class VideoPagerFragmentStateAdapter(private val items: List<User>, fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): VideoItemFragment {
        val name = items[position].username
        return VideoItemFragment.newInstance(name)
    }
    override fun getItemCount(): Int = items.size
}