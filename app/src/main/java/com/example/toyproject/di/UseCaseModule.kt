package com.example.toyproject.di

import com.example.domain.api.repository.MapApiRepository
import com.example.domain.api.usecase.GetDetailedSearchUsingPoiIdUseCase
import com.example.domain.api.usecase.GetReverseGeoCodingUseCase
import com.example.domain.map.repository.MapRepository
import com.example.domain.map.usecase.SetSdkAppKeyUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun provideInitializeMapUseCase(mapRepository: MapRepository): SetSdkAppKeyUseCase {
        return SetSdkAppKeyUseCase(mapRepository)
    }
    @Provides
    @Singleton
    fun provideGetReverseGeoCoding(mapApiRepository: MapApiRepository): GetReverseGeoCodingUseCase {
        return GetReverseGeoCodingUseCase(mapApiRepository)
    }
    @Provides
    @Singleton
    fun provideGetDetailedSearchUsingPoiId(mapApiRepository: MapApiRepository): GetDetailedSearchUsingPoiIdUseCase {
        return GetDetailedSearchUsingPoiIdUseCase(mapApiRepository)
    }
}