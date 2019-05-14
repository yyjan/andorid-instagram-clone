package com.example.yun.yunstagram.di.builder

import com.example.yun.yunstagram.LoginActivity
import com.example.yun.yunstagram.di.module.LoginActivityModule
import com.example.yun.yunstagram.di.scope.PerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = [LoginActivityModule::class])
    abstract fun bindLoginActivity(): LoginActivity

}