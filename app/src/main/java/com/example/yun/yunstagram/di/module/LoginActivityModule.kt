package com.example.yun.yunstagram.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yun.yunstagram.di.key.ViewModelKey
import com.example.yun.yunstagram.utilities.DaggerViewModelFactory
import com.example.yun.yunstagram.viewmodels.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class LoginActivityModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: LoginViewModel) : ViewModel
}