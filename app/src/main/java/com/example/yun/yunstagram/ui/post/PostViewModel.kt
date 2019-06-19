package com.example.yun.yunstagram.ui.post

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yun.yunstagram.data.DataRepository
import com.example.yun.yunstagram.data.DataSource
import com.example.yun.yunstagram.data.Post
import com.example.yun.yunstagram.data.State
import com.example.yun.yunstagram.utilities.getLocalDateTime
import com.example.yun.yunstagram.ui.BaseViewModel
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class PostViewModel @Inject constructor(private val repository: DataRepository) : BaseViewModel() {

    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post>
        get() = _post

    private val _updateResult = MutableLiveData<State>()
    val updateResult: LiveData<State>
        get() = _updateResult

    fun fetchPost(id: String?) {
        if (id.isNullOrEmpty()) return
        disposables += repository.getPost(id)
            .compose(loadingSingleTransformer())
            .subscribe({
                _post.value = it.value().toObject(Post::class.java)
            }) {
                it.printStackTrace()
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

    fun updateImage(uri: Uri) {
        // TODO: add disposables
        repository.uploadFile(uri, object : DataSource.UploadCallback {
            override fun onFailed(messages: String) {
                // TODO: error
            }

            override fun onSuccess(downloadUri: String) {
                _post.value = Post(picture_url = downloadUri)
            }
        })
    }

    fun makePost(messages: String): Post {
        val uid = repository.getCurrentUid()
        val time = getLocalDateTime()
        val id = uid + "_" + time
        return Post(
            id = id,
            created_time = time,
            author = uid,
            message = messages,
            picture_url = post.value?.picture_url
        )
    }

}
