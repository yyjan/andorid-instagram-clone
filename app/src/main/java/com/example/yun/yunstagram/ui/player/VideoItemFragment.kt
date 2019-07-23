package com.example.yun.yunstagram.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.yun.yunstagram.R
import kotlinx.android.synthetic.main.fragment_video_item.*

class VideoItemFragment : Fragment() {

    companion object {
        fun newInstance(name: String?) = VideoItemFragment().apply {
            arguments = Bundle().apply {
                putString("name", name)
            }
        }
    }
    private lateinit var viewModel: VideoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            tv_name.text = it.getString("name")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VideoViewModel::class.java)
        // TODO: Use the ViewModel
    }


}
