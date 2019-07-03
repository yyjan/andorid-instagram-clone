package com.example.yun.yunstagram.ui.adapters

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.utilities.loadCircleImage

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.color.gray)
            .into(view)
    }
}

@BindingAdapter("circleImageFromUrl")
fun bindCircleImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        view.loadCircleImage(imageUrl)
    }
}

@BindingAdapter("likesVisibility")
fun bindLikesVisibility(view: View, likeCount: Int) {
    view.visibility = if (likeCount > 0) View.VISIBLE else View.GONE
}