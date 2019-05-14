package com.example.yun.yunstagram.di.component

import com.example.yun.yunstagram.AppApplication
import com.example.yun.yunstagram.di.builder.ActivityBuilder
import com.example.yun.yunstagram.di.module.AppModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityBuilder::class]
)
@Singleton
interface AppComponent : AndroidInjector<AppApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<AppApplication>()
}