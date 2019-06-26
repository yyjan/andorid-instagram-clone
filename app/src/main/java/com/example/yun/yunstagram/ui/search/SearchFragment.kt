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

    companion object {
        fun newInstance() = SearchFragment()
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

        val adapter = UserAdapter(viewModel)
        binding.listSearch.adapter = adapter
        subscribeUi(adapter, binding)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.fetchUsers()
    }

    private fun subscribeUi(adapter: UserAdapter, binding: FragmentSearchBinding) {
        viewModel.users.observe(this, Observer { users ->
            if (users.isNotEmpty()) adapter.submitList(users)
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
