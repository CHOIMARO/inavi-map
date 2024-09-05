package com.example.toyproject

import android.app.Application
import android.content.Context
import com.example.domain.ContextProvider
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltAndroidApp
class ToyProjectApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

class AndroidContextProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : ContextProvider {
    override fun getContext(): Any = context
}