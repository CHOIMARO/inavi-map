package com.example.toyproject.home.view.activity

import HomeScreen
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.toyproject.home.view.activity.ui.theme.ToyProjectTheme
import com.inavi.mapsdk.maps.LocationProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToyProjectTheme {
                HomeScreen()
            }
        }
    }
}
