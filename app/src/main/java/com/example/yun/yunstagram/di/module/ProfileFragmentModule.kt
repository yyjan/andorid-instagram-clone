package com.example.yun.yunstagram.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yun.yunstagram.di.key.ViewModelKey
import com.example.yun.yunstagram.utilities.DaggerViewModelFactory
import com.example.yun.yunstagram.viewmodels.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ProfileFragmentModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel
}