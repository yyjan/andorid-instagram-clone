package com.example.yun.yunstagram.di.module

import com.example.yun.yunstagram.di.scope.PerFragment
import com.example.yun.yunstagram.views.AuthLoginFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class AuthActivityModule {
    @PerFragment
    @ContributesAndroidInjector(modules = [AuthLoginFragmentModule::class])
    internal abstract fun provideAuthLoginFragment(): AuthLoginFragment
}