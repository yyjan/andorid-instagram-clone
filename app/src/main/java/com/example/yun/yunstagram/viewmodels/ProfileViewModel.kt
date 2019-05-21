package com.example.yun.yunstagram.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yun.yunstagram.data.DataRepository
import com.example.yun.yunstagram.data.State
import com.example.yun.yunstagram.data.User
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val repository: DataRepository) : BaseViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _updateResult = MutableLiveData<State>()
    val updateResult: LiveData<State>
        get() = _updateResult

    fun fetchUserData() {
        val uid = repository.getCurrentUid()
        if (uid.isNullOrEmpty()) return
        disposables += repository.getUser(uid)
            .subscribe({
                _user.value = it.value().toObject(User::class.java)
            }) {
                it.printStackTrace()
            }
    }

    fun updateUser(username: String, website: String, bio: String) {
        val user = User(
            user.value?.uid,
            user.value?.email,
            username,
            bio,
            website,
            user.value?.media_count,
            user.value?.followers_count,
            user.value?.follows_count,
            user.value?.profile_picture_url
        )
        disposables += repository.updateUser(user)
            .subscribe({
                _updateResult.value = State(isSuccess = true)
            }) {
                _updateResult.value = State(errorMessages = it.message)
            }
    }


}
