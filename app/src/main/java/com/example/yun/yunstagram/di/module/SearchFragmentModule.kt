package com.example.yun.yunstagram.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yun.yunstagram.di.key.ViewModelKey
import com.example.yun.yunstagram.utilities.DaggerViewModelFactory
import com.example.yun.yunstagram.ui.profile.ProfileViewModel
import com.example.yun.yunstagram.ui.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class SearchFragmentModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel
}