package com.example.yun.yunstagram.views

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.databinding.FragmentPostEditBinding
import com.example.yun.yunstagram.viewmodels.PostViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_post_edit.*
import javax.inject.Inject

class PostEditFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PostViewModel

    companion object {
        fun newInstance() = PostEditFragment()
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
                activity?.finish()
            } else {
                Toast.makeText(activity, state.errorMessages, Toast.LENGTH_SHORT).show()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
}
