package com.example.data.di

import com.example.data.sdk.InaviService
import com.example.data.sdk.InaviServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    @Singleton
    abstract fun bindInaviService(inaviServiceImpl: InaviServiceImpl): InaviService
}