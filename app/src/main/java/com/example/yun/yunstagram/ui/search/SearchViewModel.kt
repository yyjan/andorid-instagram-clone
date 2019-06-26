package com.example.yun.yunstagram.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yun.yunstagram.data.DataRepository
import com.example.yun.yunstagram.data.User
import com.example.yun.yunstagram.ui.BaseViewModel
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val repository: DataRepository) : BaseViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users


    private val _openProfile = MutableLiveData<String>()
    val openProfile: LiveData<String>
        get() = _openProfile

    fun fetchUsers() {
        disposables += repository.getUsers()
            .compose(loadingSingleTransformer())
            .subscribe({ queryDocumentSnapshots ->
                _users.value = queryDocumentSnapshots.value().toObjects(User::class.java)
            }) {
                it.printStackTrace()
            }
    }

    fun onClickUserInfo(user: User) {
        _openProfile.value = user.uid
    }

}
