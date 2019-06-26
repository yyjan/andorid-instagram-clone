package com.example.yun.yunstagram.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yun.yunstagram.data.User
import com.example.yun.yunstagram.databinding.ListItemUserBinding
import com.example.yun.yunstagram.ui.search.SearchItemUserActionsListener
import com.example.yun.yunstagram.ui.search.SearchViewModel

class UserAdapter(private val viewModel: SearchViewModel) : ListAdapter<User, UserAdapter.ViewHolder>(UserDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.apply {
            bind(createOnClickListener(user.uid), user)
            itemView.tag = user
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false), viewModel)
    }

    private fun createOnClickListener(postId: String?): View.OnClickListener {
        return View.OnClickListener {

        }
    }

    class ViewHolder(
        private val binding: ListItemUserBinding,
        private val viewModel: SearchViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: User) {
            val userActionsListener = object : SearchItemUserActionsListener {
                override fun onUserClicked(user: User) {
                    viewModel.onClickUserInfo(user)
                }
            }

            binding.apply {
                clickListener = listener
                actionsListener = userActionsListener
                user = item
                executePendingBindings()
            }
        }
    }
}

private class UserDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}