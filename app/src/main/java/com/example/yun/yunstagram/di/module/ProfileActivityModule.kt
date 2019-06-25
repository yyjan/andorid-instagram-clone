package com.example.yun.yunstagram.di.module

import com.example.yun.yunstagram.di.scope.PerFragment
import com.example.yun.yunstagram.ui.profile.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ProfileActivityModule {
    @PerFragment
    @ContributesAndroidInjector(modules = [ProfileFragmentModule::class])
    internal abstract fun provideProfileFragment(): ProfileFragment
}