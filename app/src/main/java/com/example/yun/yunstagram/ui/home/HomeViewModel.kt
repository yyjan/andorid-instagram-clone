package com.example.yun.yunstagram.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androidhuman.rxfirebase2.firestore.model.Empty
import com.example.yun.yunstagram.data.DataRepository
import com.example.yun.yunstagram.data.Post
import com.example.yun.yunstagram.data.State
import com.example.yun.yunstagram.data.User
import com.example.yun.yunstagram.ui.BaseViewModel
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val repository: DataRepository) : BaseViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts

    private val _openPost = MutableLiveData<String>()
    val openPost: LiveData<String>
        get() = _openPost

    private val _openProfile = MutableLiveData<String>()
    val openProfile: LiveData<String>
        get() = _openProfile

    private val _updateResult = MutableLiveData<State>()
    val updateResult: LiveData<State>
        get() = _updateResult

    private val postList: MutableList<Post> = arrayListOf()

    fun fetchPosts() {
        disposables += repository.getHomePosts()
            .compose(loadingSingleTransformer())
            .subscribe({ queryDocumentSnapshots ->
                if (queryDocumentSnapshots !is Empty) {
                    val postList = queryDocumentSnapshots.value().toObjects(Post::class.java)
                    fetchUserData(postList)
                }
            }) {
                it.printStackTrace()
            }
    }

    private fun updatePostValue(id: String?, value: Map<String, Any?>) {
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

    fun fetchUserData(posts: List<Post>) {
        posts.forEachIndexed { index, post ->
            val author = post.author
            if (author.isNullOrEmpty()) return
            disposables += repository.getUser(author)
                .compose(loadingSingleTransformer())
                .subscribe({
                    val user = it.value().toObject(User::class.java)
                    user?.let { author ->
                        post.user = author
                        postList.add(post)
                        _posts.value = postList
                    }
                }) {
                    it.printStackTrace()
                }
        }
    }

    fun openPost(postId: String?) {
        _openPost.value = postId
    }

    fun onClickUserInfo(user: User) {
        _openProfile.value = user.uid
    }
}
