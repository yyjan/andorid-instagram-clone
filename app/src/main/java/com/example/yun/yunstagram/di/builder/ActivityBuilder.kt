package com.example.yun.yunstagram.di.builder

import com.example.yun.yunstagram.views.AuthActivity
import com.example.yun.yunstagram.di.module.AuthActivityModule
import com.example.yun.yunstagram.di.scope.PerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = [AuthActivityModule::class])
    abstract fun bindLoginActivity(): AuthActivity

}