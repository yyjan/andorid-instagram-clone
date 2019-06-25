package com.example.yun.yunstagram.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yun.yunstagram.data.*
import com.example.yun.yunstagram.ui.BaseViewModel
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val repository: DataRepository) : BaseViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts

    private val _updateResult = MutableLiveData<State>()
    val updateResult: LiveData<State>
        get() = _updateResult

    private val _logOutState = MutableLiveData<Boolean>()
    val logOutState: LiveData<Boolean>
        get() = _logOutState

    private val _uploadImageResult = MutableLiveData<State>()
    val uploadImageResult: LiveData<State>
        get() = _uploadImageResult

    private val _followState = MutableLiveData<Boolean>()
    val followState: LiveData<Boolean>
        get() = _followState

    private val _ownerState = MutableLiveData<Boolean>()
    val ownerState: LiveData<Boolean>
        get() = _ownerState

    private val _openPost = MutableLiveData<String>()
    val openPost: LiveData<String>
        get() = _openPost

    fun fetchUserData() {
        val uid = repository.getCurrentUid()
        if (uid.isNullOrEmpty()) return
        disposables += repository.getUser(uid)
            .compose(loadingSingleTransformer())
            .subscribe({
                _user.value = it.value().toObject(User::class.java)
                _followState.value = user.value?.followers?.contains(uid)
            }) {
                it.printStackTrace()
            }
    }

    fun fetchPosts() {
        val uid = repository.getCurrentUid()
        if (uid.isNullOrEmpty()) return
        disposables += repository.getMyPosts(uid)
            .compose(loadingSingleTransformer())
            .subscribe({ queryDocumentSnapshots ->
                _posts.value = queryDocumentSnapshots.value().toObjects(Post::class.java)
            }) {
                it.printStackTrace()
            }
    }

    fun logOut() {
        disposables += repository.signOut()
            .subscribe({
                _logOutState.postValue(true)
            }) {
                _logOutState.postValue(false)
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

    private fun updateUserValue(uid: String, value: Map<String, Any?>) {
        disposables += repository.updateUserValue(uid, value)
            .compose(loadingCompletableTransformer())
            .subscribe({
                // reload data
                fetchUserData()
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

    fun checkOwner(uid: String?) {
        _ownerState.value = repository.getCurrentUid().equals(uid)
    }

    fun onClickFollow(user: User) {
        val uid = repository.getCurrentUid()
        uid?.let { uid ->
            val userMap = mutableMapOf<String, Any?>()
            val followers = arrayListOf<String?>()
            user.followers?.let {
                followers.addAll(it)
            }
            followers.add(uid)
            userMap["followers"] = followers
            updateUserValue(uid, userMap)
        }
    }

    fun openPost(postId: String?) {
        _openPost.value = postId
    }

    fun makeUser(username: String, website: String, bio: String): User {
        return User(
            this.user.value?.uid,
            this.user.value?.email,
            username,
            bio,
            website,
            this.user.value?.media_count,
            this.user.value?.followers,
            this.user.value?.following,
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
            this.user.value?.followers,
            this.user.value?.following,
            this.user.value?.profile_picture_url
        )
    }
}
