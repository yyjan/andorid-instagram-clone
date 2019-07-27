package com.example.yun.yunstagram.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.data.User
import com.example.yun.yunstagram.databinding.FragmentSearchBinding
import com.example.yun.yunstagram.ui.adapters.UserAdapter
import com.example.yun.yunstagram.ui.profile.ProfileActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SearchFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SearchViewModel

    private var searchView: SearchView? = null

    private var uid: String? = null

    private var postId: String? = null

    private var searchType: String? = null

    companion object {
        const val ARGUMENT_UID = "UID"
        const val ARGUMENT_POST_ID = "POST_ID"
        const val ARGUMENT_SEARCH_TYPE = "SEARCH_TYPE"

        fun newInstance(uid: String? = null, postId:String? = null, type: String? = null) = SearchFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_UID, uid)
                putString(ARGUMENT_POST_ID, postId)
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
        setHasOptionsMenu(searchType.equals(SearchListType.USERS.name))
        changeTitle()

        val adapter = UserAdapter(viewModel)
        binding.listSearch.adapter = adapter
        subscribeUi(adapter, binding)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView?.let {
            it.maxWidth = Integer.MAX_VALUE
            it.queryHint = getString(R.string.hint_search_user)
            it.setIconifiedByDefault(false)
            it.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.searchUser(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isBlank()) {
                        viewModel.fetchUsers()
                    }
                    return true
                }
            })
        }

        super.onCreateOptionsMenu(menu, inflater)
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
            SearchListType.USERS_LIKES.name -> {
                viewModel.fetchLikes(postId)
            }
            else -> {
                viewModel.fetchUsers()
            }
        }
    }

    private fun setupIntent() {
        uid = arguments?.getString(ARGUMENT_UID)
        postId = arguments?.getString(ARGUMENT_POST_ID)
        searchType = arguments?.getString(ARGUMENT_SEARCH_TYPE)
    }

    private fun subscribeUi(adapter: UserAdapter, binding: FragmentSearchBinding) {
        viewModel.users.observe(this, Observer { users ->
            if (users.isNotEmpty()) adapter.submitList(users)
        })

        viewModel.followers.observe(this, Observer { followers ->
            if (followers.isNotEmpty()) {
                adapter.submitList(followers)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.followings.observe(this, Observer { followings ->
            if (followings.isNotEmpty()) {
                adapter.submitList(followings)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.likes.observe(this, Observer { likes ->
            if (likes.isNotEmpty()) {
                adapter.submitList(likes)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.openProfile.observe(this, Observer {
            openProfileDetails(it)
        })
    }

    private fun changeTitle() {
        activity?.title = when (searchType) {
            SearchListType.USERS_FOLLOWS.name -> {
                getString(R.string.title_search_followers)
            }
            SearchListType.USERS_FOLLOWINGS.name -> {
                getString(R.string.title_search_following)
            }
            SearchListType.USERS_LIKES.name -> {
                getString(R.string.title_search_likes)
            }
            else -> {
                getString(R.string.title_search)
            }
        }
    }

    private fun openProfileDetails(user: User) {
        val intent = Intent(activity, ProfileActivity::class.java).apply {
            putExtra(ProfileActivity.EXTRA_UID, user.uid)
            putExtra(ProfileActivity.EXTRA_USER_NAME, user.username)
        }
        startActivity(intent)
    }

}
