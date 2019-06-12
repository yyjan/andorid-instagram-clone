package com.example.yun.yunstagram.di.module

import com.example.yun.yunstagram.di.scope.PerFragment
import com.example.yun.yunstagram.views.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class PostEditActivityModule {
    @PerFragment
    @ContributesAndroidInjector(modules = [PostFragmentModule::class])
    internal abstract fun providePostEditFragment(): PostEditFragment
}