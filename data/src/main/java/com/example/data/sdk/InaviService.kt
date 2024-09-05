package com.example.data.sdk

import android.content.Context
import com.inavi.mapsdk.maps.MapStyle

interface InaviService {
    suspend fun initialize(context: Context)
    fun setAuthFailureCallback(callback: (Int, String) -> Unit)
    fun setAuthSuccessCallback(callback: (List<MapStyle>) -> Unit)
}