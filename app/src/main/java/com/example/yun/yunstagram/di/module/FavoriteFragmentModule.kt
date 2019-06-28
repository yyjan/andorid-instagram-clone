package com.example.yun.yunstagram.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yun.yunstagram.di.key.ViewModelKey
import com.example.yun.yunstagram.ui.favorite.FavoriteViewModel
import com.example.yun.yunstagram.utilities.DaggerViewModelFactory
import com.example.yun.yunstagram.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class FavoriteFragmentModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    internal abstract fun bindFavoriteViewModel(viewModel: FavoriteViewModel): ViewModel
}