package com.example.yun.yunstagram.utilities

import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.example.yun.yunstagram.GlideApp

fun ImageView?.loadCircleImage(url: String?){
    this?.let {
        GlideApp.with(this).load(url)
            .apply(RequestOptions.circleCropTransform())
            .override(this.width / 2, this.height / 2)
            .into(this)
    }
}