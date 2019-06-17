package com.example.yun.yunstagram.ui.auth

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yun.yunstagram.data.DataRepository
import com.example.yun.yunstagram.data.State
import com.example.yun.yunstagram.data.User
import com.example.yun.yunstagram.ui.BaseViewModel
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val repository: DataRepository) : BaseViewModel() {

    private val _loginResult = MutableLiveData<State>()
    val loginResult: LiveData<State>
        get() = _loginResult

    private val _autoLoginState = MutableLiveData<Boolean>()
    val autoLoginState: LiveData<Boolean>
        get() = _autoLoginState

    fun checkAutoLogin(){
        if (repository.getCurrentUser() != null){
            _autoLoginState.postValue(true)
        } else {
            _autoLoginState.postValue(false)
        }
    }

    fun login(email: String, password: String) {
        if (!isEmailValid(email)) return
        if (!isPasswordValid(password)) return

        disposables += repository.login(email, password)
            .subscribe({
                _loginResult.value = State(isSuccess = true)
            }) {
                _loginResult.value = State(errorMessages = it.message)
            }
    }

    fun signup(email: String, password: String, username: String) {
        if (!isEmailValid(email)) return
        if (!isPasswordValid(password)) return

        disposables += repository.signUp(email, password)
            .subscribe({
                updateUser(it.uid, it.email, username)
            }) {
                _loginResult.value = State(errorMessages = it.message)
            }
    }

    fun updateUser(uid: String, email: String?, username: String) {
        if (uid.isEmpty()) return

        val user = User(uid, email, username)
        disposables += repository.updateUser(user)
            .subscribe({
                _loginResult.value = State(isSuccess = true)
            }) {
                _loginResult.value = State(errorMessages = it.message)
            }
    }

    // username check
    private fun isEmailValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

}