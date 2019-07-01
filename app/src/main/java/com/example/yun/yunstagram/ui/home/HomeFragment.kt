package com.example.yun.yunstagram.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.databinding.FragmentHomeBinding
import com.example.yun.yunstagram.ui.adapters.PostAdapter
import com.example.yun.yunstagram.ui.adapters.PostViewType
import com.example.yun.yunstagram.ui.post.PostDetailActivity
import com.example.yun.yunstagram.ui.profile.ProfileActivity
import com.example.yun.yunstagram.utilities.Constants
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: HomeViewModel

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater, R.layout.fragment_home, container, false
        ).apply {
            viewmodel = viewModel
            lifecycleOwner = this@HomeFragment
        }

        val adapter = PostAdapter(viewModel, PostViewType.HOME)
        binding.listPost.adapter = adapter
        subscribeUi(adapter, binding)
        changeTitle()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.fetchPosts()
    }

    private fun subscribeUi(adapter: PostAdapter, binding: FragmentHomeBinding) {
        viewModel.posts.observe(this, Observer { posts ->
            if (posts.isNotEmpty()) adapter.submitList(posts)
        })

        viewModel.openPost.observe(this, Observer { postId ->
            openPostDetails(postId)
        })

        viewModel.openProfile.observe(this, Observer {
            openProfileDetails(it)
        })
    }

    private fun changeTitle() {
        activity?.title = getString(R.string.app_name)
    }

    private fun openPostDetails(postId: String) {
        val intent = Intent(activity, PostDetailActivity::class.java).apply {
            putExtra(PostDetailActivity.EXTRA_POST_ID, postId)
        }
        startActivityForResult(intent, Constants.REQUEST_CODE_FOR_POST_EDIT)
    }

    private fun openProfileDetails(postId: String) {
        val intent = Intent(activity, ProfileActivity::class.java).apply {
            putExtra(ProfileActivity.EXTRA_UID, postId)
        }
        startActivity(intent)
    }
}
