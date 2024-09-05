package com.example.toyproject.di

import android.content.Context
import com.example.domain.ContextProvider
import com.example.toyproject.AndroidContextProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContextProvider(@ApplicationContext context: Context): ContextProvider {
        return AndroidContextProvider(context)
    }
}