package com.example.data.sdk

import android.content.Context
import android.util.Log
import com.inavi.mapsdk.maps.InaviMapSdk
import com.inavi.mapsdk.maps.MapStyle
import javax.inject.Inject

class InaviServiceImpl @Inject constructor() : InaviService {
    private lateinit var context: Context
    override suspend fun initialize(context: Context) {
        this.context = context
        InaviMapSdk.getInstance(context).appKey = "UAcpucmQORb1ttAdle2v"
    }

    override fun setAuthFailureCallback(callback: (Int, String) -> Unit) {
        InaviMapSdk.getInstance(context).authFailureCallback = InaviMapSdk.AuthFailureCallback { errCode, msg ->
            callback(errCode, msg)
        }
    }

    override fun setAuthSuccessCallback(callback: (List<MapStyle>) -> Unit) {
        InaviMapSdk.getInstance(context).authSuccessCallback = InaviMapSdk.AuthSuccessCallback { mapStyles ->
            callback(mapStyles)
        }
    }
}