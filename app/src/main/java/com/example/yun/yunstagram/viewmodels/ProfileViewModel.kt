package com.example.yun.yunstagram.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yun.yunstagram.data.DataRepository
import com.example.yun.yunstagram.data.DataSource
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

    private val _uploadImageResult = MutableLiveData<State>()
    val uploadImageResult: LiveData<State>
        get() = _uploadImageResult

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

    fun updateUser(user: User) {
        disposables += repository.updateUser(user)
            .subscribe({
                _updateResult.value = State(isSuccess = true)
            }) {
                _updateResult.value = State(errorMessages = it.message)
            }
    }

    fun updateUserProfileImage(user: User) {
        disposables += repository.updateUser(user)
            .subscribe({
                _uploadImageResult.value = State(isSuccess = true)
            }) {
                _uploadImageResult.value = State(errorMessages = it.message)
            }
    }

    fun updateImage(uri: Uri) {
        // TODO : add disposables
        repository.uploadFile(uri, object : DataSource.UploadCallback {
            override fun onFailed(messages: String) {
                _uploadImageResult.value = State(errorMessages = messages)
            }

            override fun onSuccess(downloadUri: String) {
                user.value?.profile_picture_url = downloadUri
                updateUserProfileImage(convertUser())
            }
        })
    }

    fun makeUser(username: String, website: String, bio: String): User {
        return User(
            this.user.value?.uid,
            this.user.value?.email,
            username,
            bio,
            website,
            this.user.value?.media_count,
            this.user.value?.followers_count,
            this.user.value?.follows_count,
            this.user.value?.profile_picture_url
        )
    }

    private fun convertUser(): User {
        return User(
            this.user.value?.uid,
            this.user.value?.email,
            this.user.value?.username,
            this.user.value?.website,
            this.user.value?.website,
            this.user.value?.media_count,
            this.user.value?.followers_count,
            this.user.value?.follows_count,
            this.user.value?.profile_picture_url
        )
    }


}
