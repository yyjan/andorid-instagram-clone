package com.example.yun.yunstagram.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androidhuman.rxfirebase2.firestore.model.Empty
import com.example.yun.yunstagram.data.*
import com.example.yun.yunstagram.ui.BaseViewModel
import com.example.yun.yunstagram.ui.search.SearchListType
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

    private val _openUsers = MutableLiveData<Any>()
    val openUsers: LiveData<Any>
        get() = _openUsers

    fun fetchUserData(uid: String?) {
        if (uid.isNullOrEmpty()) return
        disposables += repository.getUser(uid)
            .compose(loadingSingleTransformer())
            .subscribe({
                _user.value = it.value().toObject(User::class.java)
                _followState.value = user.value?.followers?.contains(getCurrentUid())
            }) {
                it.printStackTrace()
            }
    }

    fun fetchCurrentUserData() {
        fetchUserData(getCurrentUid())
    }

    fun fetchPosts(uid: String?) {
        if (uid.isNullOrEmpty()) return
        disposables += repository.getMyPosts(uid)
            .compose(loadingSingleTransformer())
            .subscribe({ queryDocumentSnapshots ->
                if (queryDocumentSnapshots !is Empty) {
                    _posts.value = queryDocumentSnapshots.value().toObjects(Post::class.java)
                }
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

    private fun updateUserValue(uid: String, value: Map<String, Any?>, reload: Boolean) {
        disposables += repository.updateUserValue(uid, value)
            .compose(loadingCompletableTransformer())
            .subscribe({
                // reload data
                if (reload) {
                    fetchUserData(uid)
                }
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
        _loadingState.value = true
        repository.uploadFile(uri, object : DataSource.UploadCallback {
            override fun onFailed(messages: String) {
                _uploadImageResult.value = State(errorMessages = messages)
                _loadingState.value = false
            }

            override fun onSuccess(downloadUri: String) {
                _user.value?.profile_picture_url = downloadUri
                _uploadImageResult.value = State(isSuccess = true)
                _loadingState.value = false
            }
        })
    }

    fun checkOwner(uid: String?) {
        _ownerState.value = repository.getCurrentUid().equals(uid)
    }

    fun openPost(postId: String?) {
        _openPost.value = postId
    }

    fun getCurrentUid(): String? {
        return repository.getCurrentUid()
    }

    fun onClickFollowers(user: User) {
        _openUsers.value = mutableMapOf(Pair("searchType", SearchListType.USERS_FOLLOWS.name), Pair("uid", user.uid))
    }

    fun onClickFollowing(user: User) {
        _openUsers.value = mutableMapOf(Pair("searchType", SearchListType.USERS_FOLLOWINGS.name), Pair("uid", user.uid))
    }

    fun onClickFollow(profileUser: User) {
        // check uid
        val currentUid = repository.getCurrentUid()
        val profileUid = profileUser.uid
        if (currentUid.isNullOrEmpty()) return
        if (profileUid.isNullOrEmpty()) return

        // increase follower
        updateFollower(profileUser, currentUid, profileUid)

        // increase following
        updateFollowing(currentUid, profileUid)
    }

    private fun updateFollower(profileUser: User, currentUid: String, profileUid: String) {
        val userMap = mutableMapOf<String, Any?>()
        val followers = arrayListOf<String?>()
        profileUser.followers?.let { list ->
            followers.addAll(list)
        }
        if (!followers.contains(currentUid)) {
            followers.add(currentUid)
            userMap["followers"] = followers
            updateUserValue(profileUid, userMap, true)
        }
    }

    private fun updateFollowing(currentUid: String, profileUid: String) {
        disposables += repository.getUser(currentUid)
            .compose(loadingSingleTransformer())
            .subscribe({
                val currentUser = it.value().toObject(User::class.java)
                currentUser?.let { user ->
                    val userMap = mutableMapOf<String, Any?>()
                    val following = arrayListOf<String?>()
                    user.following?.let { list ->
                        following.addAll(list)
                    }
                    if (!following.contains(currentUid)) {
                        following.add(profileUid)
                        userMap["following"] = following
                        updateUserValue(currentUid, userMap, false)
                    }
                }
            }) {
                it.printStackTrace()
            }
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
