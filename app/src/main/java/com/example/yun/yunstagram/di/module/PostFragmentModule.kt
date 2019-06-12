package com.example.yun.yunstagram.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yun.yunstagram.di.key.ViewModelKey
import com.example.yun.yunstagram.utilities.DaggerViewModelFactory
import com.example.yun.yunstagram.viewmodels.PostViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class PostFragmentModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PostViewModel::class)
    internal abstract fun bindPostViewModel(viewModel: PostViewModel): ViewModel
}