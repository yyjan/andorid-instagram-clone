package com.example.yun.yunstagram.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.yun.yunstagram.GlideApp
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.databinding.FragmentProfileBinding
import com.example.yun.yunstagram.ui.adapters.PostAdapter
import com.example.yun.yunstagram.ui.auth.AuthActivity
import com.example.yun.yunstagram.utilities.Constants.REQUEST_CODE_FOR_PROFILE_EDIT
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ProfileViewModel

    companion object {
        fun newInstance() = ProfileFragment()
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

        viewModel.user.observe(this, Observer {
            binding.user = it

            GlideApp.with(this)
                .load(it.profile_picture_url)
                .into(iv_avatar)
        })

        viewModel.logOutState.observe(this, Observer { isLogOut ->
            if (isLogOut) {
                activity?.finish()
                startActivity(Intent(activity, AuthActivity::class.java))
            }
        })

        val adapter = PostAdapter()
        binding.listPost.adapter = adapter
        subscribeUi(adapter)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fetchUserData()
        viewModel.fetchPosts()

        btn_edit_profile.setOnClickListener {
            startActivityForResult(Intent(activity, ProfileEditActivity::class.java), REQUEST_CODE_FOR_PROFILE_EDIT)
        }
        btn_sign_out.setOnClickListener {
            viewModel.logOut()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_FOR_PROFILE_EDIT -> {
                fetchUserData()
            }
        }
    }

    private fun subscribeUi(adapter: PostAdapter) {
        viewModel.posts.observe(this, Observer { posts ->
            if (posts.isNotEmpty()) adapter.submitList(posts)
        })
    }


    private fun fetchUserData() {
        viewModel.fetchUserData()
    }

}
