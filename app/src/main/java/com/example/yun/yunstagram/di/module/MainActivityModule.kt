package com.example.yun.yunstagram.di.module

import com.example.yun.yunstagram.di.scope.PerFragment
import com.example.yun.yunstagram.ui.favorite.FavoriteFragment
import com.example.yun.yunstagram.ui.home.HomeFragment
import com.example.yun.yunstagram.ui.profile.ProfileFragment
import com.example.yun.yunstagram.ui.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class MainActivityModule {
    @PerFragment
    @ContributesAndroidInjector(modules = [HomeFragmentModule::class])
    internal abstract fun provideHomeFragment(): HomeFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [SearchFragmentModule::class])
    internal abstract fun provideSearchFragment(): SearchFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [FavoriteFragmentModule::class])
    internal abstract fun provideFavoriteFragment(): FavoriteFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [ProfileFragmentModule::class])
    internal abstract fun provideProfileFragment(): ProfileFragment
}