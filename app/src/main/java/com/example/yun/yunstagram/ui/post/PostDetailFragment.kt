package com.example.yun.yunstagram.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.databinding.FragmentPostDetailBinding
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_post_edit.*
import javax.inject.Inject

class PostDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        const val ARGUMENT_POST_ID = "POST_ID"
        fun newInstance(taskId: String) = PostDetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_POST_ID, taskId)
            }
        }
    }

    private lateinit var viewModel: PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PostViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentPostDetailBinding>(
            inflater, R.layout.fragment_post_detail, container, false
        ).apply {
            viewmodel = viewModel
            lifecycleOwner = this@PostDetailFragment
        }

        viewModel.post.observe(this, Observer {
            binding.post = it
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.fetchPost(arguments?.getString(ARGUMENT_POST_ID))
    }
}
