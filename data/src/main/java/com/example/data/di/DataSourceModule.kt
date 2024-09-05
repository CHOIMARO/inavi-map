package com.example.data.di

import com.example.data.api.InaviMapApiService
import com.example.data.datasource.RemoteDataSource
import com.example.data.sdk.InaviService
import com.example.domain.ContextProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {
    @Singleton
    @Provides
    fun provideRemoteDataSource(
        inaviService: InaviService,
        inaviMapApiService: InaviMapApiService,
        retrofit: Retrofit,
        contextProvider: ContextProvider,
        ) = RemoteDataSource(inaviService, inaviMapApiService, retrofit, contextProvider)
}