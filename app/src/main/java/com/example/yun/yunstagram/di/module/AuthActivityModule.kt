package com.example.yun.yunstagram.di.module

import com.example.yun.yunstagram.di.scope.PerFragment
import com.example.yun.yunstagram.ui.auth.AuthFragment
import com.example.yun.yunstagram.ui.auth.AuthLoginFragment
import com.example.yun.yunstagram.ui.auth.AuthSignUpFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class AuthActivityModule {
    @PerFragment
    @ContributesAndroidInjector(modules = [AuthFragmentModule::class])
    internal abstract fun provideAuthFragment(): AuthFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [AuthLoginFragmentModule::class])
    internal abstract fun provideAuthLoginFragment(): AuthLoginFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [AuthSignUpFragmentModule::class])
    internal abstract fun provideAuthSignUpFragment(): AuthSignUpFragment
}