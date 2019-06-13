package com.example.yun.yunstagram.di.module

import com.example.yun.yunstagram.di.scope.PerFragment
import com.example.yun.yunstagram.ui.profile.ProfileEditFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ProfileEditActivityModule {
    @PerFragment
    @ContributesAndroidInjector(modules = [ProfileFragmentModule::class])
    internal abstract fun provideProfileEditFragment(): ProfileEditFragment
}