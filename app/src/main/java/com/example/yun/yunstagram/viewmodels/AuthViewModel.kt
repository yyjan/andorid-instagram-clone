package com.example.yun.yunstagram.viewmodels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yun.yunstagram.data.DataRepository
import com.example.yun.yunstagram.data.State
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val repository: DataRepository) : BaseViewModel() {

    private val _loginResult = MutableLiveData<State>()
    val loginResult: LiveData<State>
        get() = _loginResult

    fun login(email: String, password: String) {
        //if (!isUserNameValid(email)) return
        // if (!isPasswordValid(password)) return

        disposables += repository.login(email, password)
            .subscribe({
                _loginResult.value = State(isSuccess = true)
            }) {
                _loginResult.value = State(errorMessages = it.message)
            }
    }

    // username check
    private fun isUserNameValid(username: String): Boolean {
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