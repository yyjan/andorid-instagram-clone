package com.example.yun.yunstagram.di.builder

import com.example.yun.yunstagram.di.module.*
import com.example.yun.yunstagram.di.scope.PerActivity
import com.example.yun.yunstagram.ui.auth.AuthActivity
import com.example.yun.yunstagram.ui.home.MainActivity
import com.example.yun.yunstagram.ui.post.PostDetailActivity
import com.example.yun.yunstagram.ui.post.PostEditActivity
import com.example.yun.yunstagram.ui.profile.ProfileEditActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = [AuthActivityModule::class])
    abstract fun bindLoginActivity(): AuthActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [ProfileEditActivityModule::class])
    abstract fun bindProfileEditActivity(): ProfileEditActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [PostEditActivityModule::class])
    abstract fun bindPostEditActivity(): PostEditActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [PostDetailActivityModule::class])
    abstract fun bindPostDetailActivity(): PostDetailActivity

}