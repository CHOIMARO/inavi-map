package com.example.data.di

import com.choimaro.data.module.CoroutinesQualifiers
import com.example.data.api.MapApiRepositoryImpl
import com.example.data.datasource.RemoteDataSource
import com.example.data.map.MapRepositoryImpl
import com.example.data.sdk.InaviService
import com.example.domain.api.repository.MapApiRepository
import com.example.domain.map.repository.MapRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideMapRepository(
        remoteDataSource: RemoteDataSource,
        @CoroutinesQualifiers.IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): MapRepository {
        return MapRepositoryImpl(
            remoteDataSource,
            coroutineDispatcher
        )
    }

    @Provides
    @Singleton
    fun bindMapApiRepository(
        remoteDataSource: RemoteDataSource,
        @CoroutinesQualifiers.IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): MapApiRepository {
        return MapApiRepositoryImpl(
            remoteDataSource,
            coroutineDispatcher
        )
    }
}