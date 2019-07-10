package com.example.yun.yunstagram.ui.favorite

import com.example.yun.yunstagram.data.Favorite

interface FavoriteItemUserActionsListener {

    fun onItemClicked(favorite: Favorite)
}
