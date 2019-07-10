package com.example.yun.yunstagram.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yun.yunstagram.data.Favorite
import com.example.yun.yunstagram.databinding.ListItemFavoriteBinding
import com.example.yun.yunstagram.ui.favorite.FavoriteItemUserActionsListener
import com.example.yun.yunstagram.ui.favorite.FavoriteViewModel

class FavoriteAdapter(private val viewModel: FavoriteViewModel) : ListAdapter<Favorite, FavoriteAdapter.ViewHolder>(FavoriteDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = getItem(position)
        holder.apply {
            bind(createOnClickListener(favorite.id), favorite)
            itemView.tag = favorite
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.context), parent, false), viewModel)
    }

    private fun createOnClickListener(postId: String?): View.OnClickListener {
        return View.OnClickListener {

        }
    }

    class ViewHolder(
        private val binding: ListItemFavoriteBinding,
        private val viewModel: FavoriteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: Favorite) {
            val favoriteActionsListener = object : FavoriteItemUserActionsListener {
                override fun onItemClicked(favorite: Favorite) {
                }
            }

            binding.apply {
                clickListener = listener
                actionsListener = favoriteActionsListener
                favorite = item
                executePendingBindings()
            }
        }
    }
}

private class FavoriteDiffCallback : DiffUtil.ItemCallback<Favorite>() {

    override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
        return oldItem == newItem
    }
}