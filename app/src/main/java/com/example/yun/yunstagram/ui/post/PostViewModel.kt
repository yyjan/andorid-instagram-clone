package com.example.yun.yunstagram.ui.post

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yun.yunstagram.data.*
import com.example.yun.yunstagram.utilities.getLocalDateTime
import com.example.yun.yunstagram.ui.BaseViewModel
import com.example.yun.yunstagram.ui.search.SearchListType
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class PostViewModel @Inject constructor(private val repository: DataRepository) : BaseViewModel() {

    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post>
        get() = _post

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _downloadUri = MutableLiveData<String>()
    val downloadUri: LiveData<String>
        get() = _downloadUri

    private val _updateResult = MutableLiveData<State>()
    val updateResult: LiveData<State>
        get() = _updateResult

    private val _deleteState = MutableLiveData<State>()
    val deleteState: LiveData<State>
        get() = _deleteState

    private val _openProfile = MutableLiveData<User>()
    val openProfile: LiveData<User>
        get() = _openProfile

    private val _openUsers = MutableLiveData<Any>()
    val openUsers: LiveData<Any>
        get() = _openUsers

    private var editState = false

    fun fetchPost(id: String?) {
        if (id.isNullOrEmpty()) return
        disposables += repository.getPost(id)
            .compose(loadingSingleTransformer())
            .subscribe({
                _post.value = it.value().toObject(Post::class.java)
                fetchUserData()
                checkLike()
            }) {
                it.printStackTrace()
            }
    }

    fun createPost(post: Post) {
        disposables += repository.createPost(post)
            .subscribe({
                val postMap = mutableMapOf<String, Any?>()
                postMap["id"] = it.id
                updatePostValue(it.id, postMap, false)
                _updateResult.value = State(isSuccess = true)
            }) {
                _updateResult.value = State(errorMessages = it.message)
            }
    }

    fun updatePost(post: Post) {
        disposables += repository.updatePost(post)
            .compose(loadingCompletableTransformer())
            .subscribe({
                _updateResult.value = State(isSuccess = true)
            }) {
                _updateResult.value = State(errorMessages = it.message)
            }
    }

    private fun updatePostValue(id: String?, value: Map<String, Any?>, reload: Boolean) {
        id?.let {
            disposables += repository.updatePostValue(id, value)
                .compose(loadingCompletableTransformer())
                .subscribe({
                    _updateResult.value = State(isSuccess = true)
                }) {
                    _updateResult.value = State(errorMessages = it.message)
                }
        }
    }

    fun deletePost(id: String) {
        disposables += repository.deletePost(id)
            .compose(loadingCompletableTransformer())
            .subscribe({
                _deleteState.value = State(isSuccess = true)
            }) {
                _deleteState.value = State(errorMessages = it.message)
            }
    }

    fun fetchUserData() {
        val author = post.value?.author
        if (author.isNullOrEmpty()) return
        disposables += repository.getUser(author)
            .compose(loadingSingleTransformer())
            .subscribe({
                _user.value = it.value().toObject(User::class.java)
            }) {
                it.printStackTrace()
            }
    }

    fun uploadImage(uri: Uri) {
        // TODO: add disposables
        _loadingState.postValue(true)
        repository.uploadFile(uri, object : DataSource.UploadCallback {
            override fun onFailed(messages: String) {
                _loadingState.postValue(false)
                _updateResult.value = State(errorMessages = messages)
            }

            override fun onSuccess(downloadUri: String) {
                _downloadUri.value = downloadUri
                _loadingState.postValue(false)
            }
        })
    }

    fun isImageUpload() = (post.value?.picture_url != null || downloadUri.value != null)

    fun setEditState(postId: String?) {
        editState = postId != null
    }

    fun checkLike() {
        _post.value?.canLike = post.value?.likes?.contains(repository.getCurrentUid())
    }

    fun sharePost(messages: String) {
        return if (editState) {
            updatePost(makeEditPost(messages))
        } else {
            createPost(makeNewPost(messages))
        }
    }

    fun onCompleteLikeChanged(post: Post, isChecked: Boolean) {
        val currentUid = repository.getCurrentUid()
        val postMap = mutableMapOf<String, Any?>()
        val likes = arrayListOf<String?>()
        post.likes?.let { list ->
            likes.addAll(list)
        }
        if (isChecked) {
            if (!likes.contains(currentUid)) {
                likes.add(currentUid)
                postMap["likes"] = likes
                updatePostValue(post.id, postMap, true)
            }
        } else {
            if (likes.contains(currentUid)) {
                likes.remove(currentUid)
                postMap["likes"] = likes
                updatePostValue(post.id, postMap, true)
            }
        }
    }

    fun onClickUserInfo(user: User) {
        _openProfile.value = user
    }

    fun onClickLikes(post: Post) {
        _openUsers.value = mutableMapOf(Pair("searchType", SearchListType.USERS_LIKES.name), Pair("postId", post.id))
    }

    private fun makeNewPost(messages: String = ""): Post {
        val uid = repository.getCurrentUid()
        val time = getLocalDateTime()
        return Post(
            created_time = time,
            author = uid,
            message = messages,
            picture_url = downloadUri.value
        )
    }

    private fun makeEditPost(messages: String): Post {
        val uid = repository.getCurrentUid()
        val time = getLocalDateTime()
        return Post(
            id = post.value?.id,
            created_time = post.value?.created_time,
            updated_time = time,
            author = uid,
            message = messages,
            picture_url = post.value?.picture_url,
            likes = post.value?.likes
        )
    }

}
