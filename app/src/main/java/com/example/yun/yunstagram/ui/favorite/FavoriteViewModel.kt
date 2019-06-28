package com.example.yun.yunstagram.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yun.yunstagram.data.DataRepository
import com.example.yun.yunstagram.data.Favorite
import com.example.yun.yunstagram.ui.BaseViewModel
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val repository: DataRepository) : BaseViewModel() {

    private val _favorites = MutableLiveData<List<Favorite>>()
    val favorites: LiveData<List<Favorite>>
        get() = _favorites

    fun fetchFavorites(uid: String?) {
        if (uid.isNullOrEmpty()) return
        disposables += repository.getMyFavorites(uid)
            .compose(loadingSingleTransformer())
            .subscribe({

            }) {
                it.printStackTrace()
            }
    }
}
