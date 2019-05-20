package com.example.yun.yunstagram.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yun.yunstagram.data.DataRepository
import com.example.yun.yunstagram.data.User
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val repository: DataRepository) : BaseViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

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


}
