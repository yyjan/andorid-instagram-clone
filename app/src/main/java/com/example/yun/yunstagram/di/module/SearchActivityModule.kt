package com.example.yun.yunstagram.di.module

import com.example.yun.yunstagram.di.scope.PerFragment
import com.example.yun.yunstagram.ui.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class SearchActivityModule {
    @PerFragment
    @ContributesAndroidInjector(modules = [SearchFragmentModule::class])
    internal abstract fun provideSearchFragment(): SearchFragment
}