package com.example.yun.yunstagram.utilities

import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.yun.yunstagram.GlideApp
import com.example.yun.yunstagram.R

fun ImageView?.loadCircleImage(url: String?){
    this?.let {
        GlideApp.with(this).load(url)
            .apply(RequestOptions.circleCropTransform())
            .transition(DrawableTransitionOptions.withCrossFade())
            .override(this.width / 2, this.height / 2)
            .error(R.color.gray)
            .into(this)
    }
}