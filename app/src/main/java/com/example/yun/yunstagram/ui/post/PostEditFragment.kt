package com.example.yun.yunstagram.ui.post

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.yun.yunstagram.GlideApp
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.databinding.FragmentPostEditBinding
import com.example.yun.yunstagram.utilities.Constants
import com.example.yun.yunstagram.utilities.Constants.REQUEST_CODE_FOR_IMAGE
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_post_edit.*
import javax.inject.Inject

class PostEditFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PostViewModel

    private var postId: String? = null

    companion object {
        const val ARGUMENT_POST_ID = "POST_ID"
        fun newInstance(taskId: String?) = PostEditFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_POST_ID, taskId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PostViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentPostEditBinding>(
            inflater, R.layout.fragment_post_edit, container, false
        ).apply {
            viewmodel = viewModel
            lifecycleOwner = this@PostEditFragment
        }

        viewModel.updateResult.observe(this, Observer { state ->
            if (state.isSuccess) {
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            } else {
                Toast.makeText(activity, state.errorMessages, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.post.observe(this, Observer {
            binding.post = it
            showProfileImage(viewModel.post.value?.picture_url)
        })

        setHasOptionsMenu(true)
        setupIntent()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.setEditState(postId)
        viewModel.fetchPost(postId)

        btn_edit_image.setOnClickListener {
            getPhotoImages()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                val messages = et_message.text.toString()
                val post = viewModel.makePost(messages)

                viewModel.updatePost(post)
                true
            }
            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_post_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            REQUEST_CODE_FOR_IMAGE -> {
                val photoUri = data?.data
                photoUri?.let { viewModel.updateImage(it) }
            }
        }
    }

    private fun setupIntent() {
        postId = arguments?.getString(PostDetailFragment.ARGUMENT_POST_ID)
    }

    private fun showProfileImage(url: String?) {
        GlideApp.with(this).load(url)
            .into(iv_image)
    }

    private fun getPhotoImages() {
        val intent = Intent()
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "파일 선택"), REQUEST_CODE_FOR_IMAGE)
    }
}
