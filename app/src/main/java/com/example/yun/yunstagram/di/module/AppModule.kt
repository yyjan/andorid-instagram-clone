package com.example.yun.yunstagram.di.module

import com.example.yun.yunstagram.DataRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideRepository(): DataRepository = DataRepository()
}