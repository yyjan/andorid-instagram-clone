package com.example.yun.yunstagram.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.CompletableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

open class BaseViewModel : ViewModel() {
    protected val disposables = CompositeDisposable()

    val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean>
        get() = _loadingState

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    protected fun <T> loadingSingleTransformer(): SingleTransformer<T, T> {
        return SingleTransformer { observable ->
            observable.subscribeOn(
                Schedulers.io()
            ).observeOn(
                AndroidSchedulers.mainThread()
            ).doOnSubscribe {
                _loadingState.postValue(true)
            }.doFinally {
                _loadingState.postValue(false)
            }.doOnError {

            }
        }
    }

    protected fun loadingCompletableTransformer(): CompletableTransformer {
        return CompletableTransformer { observable ->
            observable.subscribeOn(
                Schedulers.io()
            ).observeOn(
                AndroidSchedulers.mainThread()
            ).doOnSubscribe {
                _loadingState.postValue(true)
            }.doFinally {
                _loadingState.postValue(false)
            }.doOnError {

            }
        }
    }
}