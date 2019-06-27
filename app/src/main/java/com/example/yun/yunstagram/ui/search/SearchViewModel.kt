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

    private val _followers = MutableLiveData<List<User>>()
    val followers: LiveData<List<User>>
        get() = _followers

    private val _openProfile = MutableLiveData<String>()
    val openProfile: LiveData<String>
        get() = _openProfile

    private val followerList: MutableList<User> = arrayListOf()

    fun fetchUsers() {
        disposables += repository.getUsers()
            .compose(loadingSingleTransformer())
            .subscribe({ queryDocumentSnapshots ->
                _users.value = queryDocumentSnapshots.value().toObjects(User::class.java)
            }) {
                it.printStackTrace()
            }
    }

    fun fetchFollowers(uid: String?) {
        if (uid.isNullOrEmpty()) return
        disposables += repository.getUser(uid)
            .compose(loadingSingleTransformer())
            .subscribe({
                val user = it.value().toObject(User::class.java)
                val followers = user?.followers

                followers?.forEach { _ ->
                    disposables += repository.getUser(uid)
                        .compose(loadingSingleTransformer())
                        .subscribe({ data ->
                            val user = data.value().toObject(User::class.java)
                            user?.let {
                                followerList.add(user)
                                _followers.value = followerList
                            }
                        }) {
                            it.printStackTrace()
                        }
                }
            }) {
                it.printStackTrace()
            }
    }

    fun onClickUserInfo(user: User) {
        _openProfile.value = user.uid
    }

}
