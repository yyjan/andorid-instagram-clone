package com.example.yun.yunstagram.ui.post

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.data.User
import com.example.yun.yunstagram.databinding.FragmentPostDetailBinding
import com.example.yun.yunstagram.ui.profile.ProfileActivity
import com.example.yun.yunstagram.ui.search.SearchActivity
import com.example.yun.yunstagram.utilities.Constants.REQUEST_CODE_FOR_PROFILE_EDIT
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_post_detail.*
import javax.inject.Inject

class PostDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var postId: String

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

        viewModel.user.observe(this, Observer {
            binding.user = it
        })

        viewModel.updateResult.observe(this, Observer { state ->
            if (state.isSuccess) {
                fetchPostData()
                activity?.setResult(Activity.RESULT_OK)
            }
        })

        viewModel.deleteState.observe(this, Observer { state ->
            if (state.isSuccess) {
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            } else {
                Toast.makeText(activity, state.errorMessages, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.openProfile.observe(this, Observer {
            openProfileDetails(it)
        })

        viewModel.openUsers.observe(this, Observer { map ->
            val maps = map as Map<String, String>
            openLikeUsers(maps["postId"].toString(), maps["searchType"].toString())
        })

        setupIntent()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fetchPostData()

        iv_more.setOnClickListener {
            showMenuDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_FOR_PROFILE_EDIT -> {
                fetchPostData()
            }
        }
    }

    private fun setupIntent() {
        arguments?.let {
            postId = it.getString(ARGUMENT_POST_ID)
        }
    }

    private fun showMenuDialog() {
        context?.let {
            val builder = AlertDialog.Builder(it)
            val menus = arrayOf("Edit", "Delete")
            builder.setItems(menus) { dialog, which ->
                when (which) {
                    0 -> {
                        openPostEdit(postId)
                    }
                    1 -> {
                        viewModel.deletePost(postId)
                    }
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun fetchPostData() {
        viewModel.fetchPost(postId)
    }

    private fun openPostEdit(postId: String) {
        val intent = Intent(activity, PostEditActivity::class.java).apply {
            putExtra(PostEditActivity.EXTRA_POST_ID, postId)
        }
        startActivityForResult(intent, REQUEST_CODE_FOR_PROFILE_EDIT)
    }

    private fun openProfileDetails(user: User) {
        val intent = Intent(activity, ProfileActivity::class.java).apply {
            putExtra(ProfileActivity.EXTRA_UID, user.uid)
            putExtra(ProfileActivity.EXTRA_USER_NAME, user.username)
        }
        startActivity(intent)
    }

    private fun openLikeUsers(postId: String, searchType: String) {
        val intent = Intent(activity, SearchActivity::class.java).apply {
            putExtra(SearchActivity.EXTRA_POST_ID, postId)
            putExtra(SearchActivity.EXTRA_SEARCH_TYPE, searchType)
        }
        startActivity(intent)
    }
}
