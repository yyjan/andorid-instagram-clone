package com.example.yun.yunstagram.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yun.yunstagram.data.Post
import com.example.yun.yunstagram.databinding.ListItemHomePostBinding
import com.example.yun.yunstagram.databinding.ListItemPostBinding
import com.example.yun.yunstagram.ui.home.HomeViewModel
import com.example.yun.yunstagram.ui.post.PostItemUserActionsListener
import com.example.yun.yunstagram.ui.profile.ProfileViewModel

class PostAdapter(private val viewModel: Any, private val viewType: PostViewType) :
    ListAdapter<Post, PostAdapter.ViewHolder>(PostDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.apply {
            bind(createOnClickListener(post.id), post)
            itemView.tag = post
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return when (viewType) {
            PostViewType.PROFILE.type -> ProfileViewHolder(
                ListItemPostBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), viewModel
            )
            else -> HomeViewHolder(
                ListItemHomePostBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), viewModel
            )

        }
    }

    private fun createOnClickListener(postId: String?): View.OnClickListener {
        return View.OnClickListener {

        }
    }

    abstract class ViewHolder(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        abstract fun bind(listener: View.OnClickListener, item: Post)
    }

    class ProfileViewHolder(
        private val binding: ListItemPostBinding,
        private val viewModel: Any
    ) : ViewHolder(binding) {
        override fun bind(listener: View.OnClickListener, item: Post) {
            val userActionsListener = object : PostItemUserActionsListener {
                override fun onPostClicked(post: Post) {
                    (viewModel as ProfileViewModel).openPost(post.id)
                }
            }

            binding.apply {
                clickListener = listener
                actionsListener = userActionsListener
                post = item
                executePendingBindings()
            }
        }
    }

    class HomeViewHolder(
        private val binding: ListItemHomePostBinding,
        private val viewModel: Any
    ) : ViewHolder(binding) {
        override fun bind(listener: View.OnClickListener, item: Post) {
            val userActionsListener = object : PostItemUserActionsListener {
                override fun onPostClicked(post: Post) {
                    (viewModel as HomeViewModel).openPost(post.id)
                }
            }

            binding.apply {
                actionsListener = userActionsListener
                post = item
                executePendingBindings()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType.type
    }
}

enum class PostViewType(val type: Int) {
    HOME(0),
    PROFILE(1)
}

private class PostDiffCallback : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}