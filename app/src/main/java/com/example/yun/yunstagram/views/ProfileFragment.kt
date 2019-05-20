package com.example.yun.yunstagram.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.databinding.FragmentProfileBinding
import com.example.yun.yunstagram.viewmodels.ProfileViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ProfileFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var profileViewModel: ProfileViewModel

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        profileViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentProfileBinding>(
            inflater, R.layout.fragment_profile, container, false
        ).apply {
            viewmodel = profileViewModel
            lifecycleOwner = this@ProfileFragment
        }

        profileViewModel.user.observe(this, Observer {
            binding.user = it
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileViewModel.fetchUserData()

    }
}
