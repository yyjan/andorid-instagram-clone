package com.example.yun.yunstagram.ui.player

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2

import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.data.User
import kotlinx.android.synthetic.main.fragment_video.*

class VideoFragment : Fragment() {

    companion object {
        fun newInstance() = VideoFragment()
    }

    private lateinit var viewModel: VideoViewModel

    private var users: MutableList<User> = mutableListOf(
        User(username = "user1"),
        User(username = "user2"),
        User(username = "user3"),
        User(username = "user4"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VideoViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_pager.adapter = VideoPagerFragmentStateAdapter(users, this)
        view_pager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }


}
