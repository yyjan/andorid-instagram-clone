package com.example.yun.yunstagram.di.module

import com.example.yun.yunstagram.di.scope.PerFragment
import com.example.yun.yunstagram.ui.post.PostDetailFragment
import com.example.yun.yunstagram.ui.post.PostEditFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class PostDetailActivityModule {
    @PerFragment
    @ContributesAndroidInjector(modules = [PostFragmentModule::class])
    internal abstract fun providePostDetailFragment(): PostDetailFragment
}