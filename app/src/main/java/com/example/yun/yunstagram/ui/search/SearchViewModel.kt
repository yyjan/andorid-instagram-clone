package com.example.yun.yunstagram.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androidhuman.rxfirebase2.firestore.model.Empty
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

    private val _followings = MutableLiveData<List<User>>()
    val followings: LiveData<List<User>>
        get() = _followings

    private val _openProfile = MutableLiveData<User>()
    val openProfile: LiveData<User>
        get() = _openProfile

    private val followerList: MutableList<User> = arrayListOf()
    private val followingList: MutableList<User> = arrayListOf()

    fun fetchUsers() {
        disposables += repository.getUsers()
            .compose(loadingSingleTransformer())
            .subscribe({ queryDocumentSnapshots ->
                _users.value = queryDocumentSnapshots.value().toObjects(User::class.java)
            }) {
                it.printStackTrace()
            }
    }

    fun searchUser(userName: String) {
        disposables += repository.getSearchUser(userName)
            .compose(loadingSingleTransformer())
            .subscribe({ queryDocumentSnapshots ->
                if (queryDocumentSnapshots !is Empty) {
                    _users.value = queryDocumentSnapshots.value().toObjects(User::class.java)
                }
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

                followers?.forEach { id ->
                    disposables += repository.getUser(id)
                        .compose(loadingSingleTransformer())
                        .subscribe({ data ->
                            val follower = data.value().toObject(User::class.java)
                            follower?.let {
                                followerList.add(follower)
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

    fun fetchFollowings(uid: String?) {
        if (uid.isNullOrEmpty()) return
        disposables += repository.getUser(uid)
            .compose(loadingSingleTransformer())
            .subscribe({
                val user = it.value().toObject(User::class.java)
                val followings = user?.following

                followings?.forEach { id ->
                    disposables += repository.getUser(id)
                        .compose(loadingSingleTransformer())
                        .subscribe({ data ->
                            val following = data.value().toObject(User::class.java)
                            following?.let {
                                followingList.add(following)
                                _followings.value = followingList
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
        _openProfile.value = user
    }

}
