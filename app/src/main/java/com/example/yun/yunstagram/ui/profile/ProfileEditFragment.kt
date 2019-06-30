package com.example.yun.yunstagram.ui.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.yun.yunstagram.GlideApp
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.databinding.FragmentProfileEditBinding
import com.example.yun.yunstagram.utilities.Constants
import com.example.yun.yunstagram.utilities.loadCircleImage
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import javax.inject.Inject

class ProfileEditFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var profileViewModel: ProfileViewModel

    private var photoUri: Uri? = null

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

        profileViewModel.uploadImageResult.observe(this, Observer { state ->
            if (state.isSuccess) {
                showProfileImage(photoUri.toString())
            } else {
                Toast.makeText(activity, state.errorMessages, Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileViewModel.fetchCurrentUserData()

        btn_save.setOnClickListener {
            val userName = et_name.text.toString().trim()
            val website = et_website.text.toString().trim()
            val bio = et_bio.text.toString().trim()
            val user = profileViewModel.makeUser(userName, website, bio)

            profileViewModel.updateUser(user)
        }

        tv_change_photo.setOnClickListener {
            getPhotoImages()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return

        when (requestCode) {
            Constants.REQUEST_CODE_FOR_IMAGE -> {
                photoUri = data?.data
                photoUri?.let { profileViewModel.updateImage(it) }
            }
        }
    }

    private fun showProfileImage(url: String?) {
        iv_avatar.loadCircleImage(url)
    }

    private fun getPhotoImages() {
        val intent = Intent()
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "파일 선택"), Constants.REQUEST_CODE_FOR_IMAGE)
    }


}
