package com.example.yun.yunstagram.ui.search

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
import com.example.yun.yunstagram.databinding.FragmentSearchBinding
import com.example.yun.yunstagram.ui.adapters.UserAdapter
import com.example.yun.yunstagram.ui.profile.ProfileActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SearchFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SearchViewModel

    private var uid: String? = null

    private var searchType: String? = null

    companion object {
        const val ARGUMENT_UID = "UID"
        const val ARGUMENT_SEARCH_TYPE = "SEARCH_TYPE"

        fun newInstance(uid: String? = null, type: String? = null) = SearchFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_UID, uid)
                putString(ARGUMENT_SEARCH_TYPE, type)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentSearchBinding>(
            inflater, R.layout.fragment_search, container, false
        ).apply {
            viewmodel = viewModel
            lifecycleOwner = this@SearchFragment
        }

        setupIntent()
        val adapter = UserAdapter(viewModel)
        binding.listSearch.adapter = adapter
        subscribeUi(adapter, binding)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        when (searchType) {
            SearchListType.USERS_FOLLOWS.name -> {
                viewModel.fetchFollowers(uid)
            }
            SearchListType.USERS_FOLLOWINGS.name -> {
                viewModel.fetchFollowings(uid)
            }
            else -> {
                viewModel.fetchUsers()
            }
        }
    }

    private fun setupIntent() {
        uid = arguments?.getString(ARGUMENT_UID)
        searchType = arguments?.getString(ARGUMENT_SEARCH_TYPE)
    }

    private fun subscribeUi(adapter: UserAdapter, binding: FragmentSearchBinding) {
        viewModel.users.observe(this, Observer { users ->
            if (users.isNotEmpty()) adapter.submitList(users)
        })

        viewModel.followers.observe(this, Observer { followers ->
            if (followers.isNotEmpty()) adapter.submitList(followers)
        })

        viewModel.followings.observe(this, Observer { followings ->
            if (followings.isNotEmpty()) adapter.submitList(followings)
        })

        viewModel.openProfile.observe(this, Observer {
            openProfileDetails(it)
        })
    }

    private fun openProfileDetails(postId: String) {
        val intent = Intent(activity, ProfileActivity::class.java).apply {
            putExtra(ProfileActivity.EXTRA_UID, postId)
        }
        startActivity(intent)
    }

}