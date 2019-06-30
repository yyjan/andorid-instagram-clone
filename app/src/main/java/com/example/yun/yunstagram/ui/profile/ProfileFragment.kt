package com.example.yun.yunstagram.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.databinding.FragmentProfileBinding
import com.example.yun.yunstagram.ui.adapters.PostAdapter
import com.example.yun.yunstagram.ui.adapters.PostViewType
import com.example.yun.yunstagram.ui.auth.AuthActivity
import com.example.yun.yunstagram.ui.post.PostDetailActivity
import com.example.yun.yunstagram.ui.search.SearchActivity
import com.example.yun.yunstagram.utilities.Constants.REQUEST_CODE_FOR_POST_EDIT
import com.example.yun.yunstagram.utilities.Constants.REQUEST_CODE_FOR_PROFILE_EDIT
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ProfileViewModel

    private var uid: String? = null

    companion object {
        const val ARGUMENT_UID = "UID"
        fun newInstance(uid: String? = null) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_UID, uid)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentProfileBinding>(
            inflater, R.layout.fragment_profile, container, false
        ).apply {
            viewmodel = viewModel
            lifecycleOwner = this@ProfileFragment
        }

        val adapter = PostAdapter(viewModel, PostViewType.PROFILE)
        binding.listPost.adapter = adapter
        setupIntent()
        subscribeUi(adapter, binding)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fetchUserData()
        fetchPostData()
        viewModel.checkOwner(uid)

        btn_edit_profile.setOnClickListener {
            openProfileEdit()
        }
        btn_sign_out.setOnClickListener {
            viewModel.logOut()
        }

        list_post.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        list_post.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_FOR_PROFILE_EDIT -> {
                fetchUserData()
            }
            REQUEST_CODE_FOR_POST_EDIT -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        fetchPostData()
                    }
                }
            }
        }
    }

    private fun setupIntent() {
        uid = arguments?.getString(ARGUMENT_UID)
    }

    private fun subscribeUi(adapter: PostAdapter, binding: FragmentProfileBinding) {
        viewModel.user.observe(this, Observer {
            binding.user = it
        })

        viewModel.logOutState.observe(this, Observer { isLogOut ->
            if (isLogOut) {
                activity?.finish()
                startActivity(Intent(activity, AuthActivity::class.java))
            }
        })

        viewModel.openPost.observe(this, Observer { postId ->
            openPostDetails(postId)
        })

        viewModel.openUsers.observe(this, Observer { map ->
            val maps = map as Map<String, String>
            openUsers(maps["uid"].toString(), maps["searchType"].toString())
        })

        viewModel.posts.observe(this, Observer { posts ->
            if (posts.isNotEmpty()) adapter.submitList(posts)
        })
    }

    private fun fetchUserData() {
        viewModel.fetchUserData(uid)
    }

    private fun fetchPostData() {
        viewModel.fetchPosts(uid)
    }

    private fun openProfileEdit() {
        startActivityForResult(Intent(activity, ProfileEditActivity::class.java), REQUEST_CODE_FOR_PROFILE_EDIT)
    }

    private fun openPostDetails(postId: String) {
        val intent = Intent(activity, PostDetailActivity::class.java).apply {
            putExtra(PostDetailActivity.EXTRA_POST_ID, postId)
        }
        startActivityForResult(intent, REQUEST_CODE_FOR_POST_EDIT)
    }

    private fun openUsers(uid: String, searchType: String) {
        val intent = Intent(activity, SearchActivity::class.java).apply {
            putExtra(SearchActivity.EXTRA_UID, uid)
            putExtra(SearchActivity.EXTRA_SEARCH_TYPE, searchType)
        }
        startActivity(intent)
    }

}
