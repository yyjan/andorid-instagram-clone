package com.example.yun.yunstagram.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.databinding.FragmentFavoriteBinding
import com.example.yun.yunstagram.ui.adapters.FavoriteAdapter
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class FavoriteFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: FavoriteViewModel

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FavoriteViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentFavoriteBinding>(
            inflater, R.layout.fragment_favorite, container, false
        ).apply {
            viewmodel = viewModel
            lifecycleOwner = this@FavoriteFragment
        }

        val adapter = FavoriteAdapter(viewModel)
        binding.listFavorite.adapter = adapter
        subscribeUi(adapter, binding)
        changeTitle()

        return binding.root
    }

    private fun subscribeUi(adapter: FavoriteAdapter, binding: FragmentFavoriteBinding) {
        viewModel.favorites.observe(this, Observer { favorites ->
            if (favorites.isNotEmpty()) adapter.submitList(favorites)
        })
    }

    private fun changeTitle() {
        activity?.title = getString(R.string.title_favorite)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}
