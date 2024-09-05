package com.example.toyproject.splash.view.screen

import android.content.Intent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.finishAffinity
import com.example.toyproject.R
import com.example.toyproject.home.view.activity.MainActivity
import com.example.toyproject.ui.compose.LocationPermissionRequest
import com.example.toyproject.ui.compose.adaptiveIconPainterResource
import kotlinx.coroutines.delay

@Composable
fun ShowSplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = adaptiveIconPainterResource(id = R.mipmap.ic_launcher_round),
            contentDescription = "App Icon",
            modifier = Modifier.size(100.dp)
        )
    }
}