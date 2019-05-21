package com.example.yun.yunstagram.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.databinding.FragmentProfileBinding
import com.example.yun.yunstagram.databinding.FragmentProfileEditBinding
import com.example.yun.yunstagram.viewmodels.ProfileViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import javax.inject.Inject

class ProfileEditFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var profileViewModel: ProfileViewModel

    companion object {
        fun newInstance() = ProfileEditFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        profileViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentProfileEditBinding>(
            inflater, R.layout.fragment_profile_edit, container, false
        ).apply {
            viewmodel = profileViewModel
            lifecycleOwner = this@ProfileEditFragment
        }

        profileViewModel.user.observe(this, Observer {
            binding.user = it
        })

        profileViewModel.updateResult.observe(this, Observer { state ->
            if (state.isSuccess) {
                activity?.finish()
            } else {
                Toast.makeText(activity, state.errorMessages, Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileViewModel.fetchUserData()

        btn_save.setOnClickListener {
            val userName = et_name.text.toString().trim()
            val website = et_website.text.toString().trim()
            val bio = et_bio.text.toString().trim()
            profileViewModel.updateUser(userName, website, bio)
        }
    }

}
