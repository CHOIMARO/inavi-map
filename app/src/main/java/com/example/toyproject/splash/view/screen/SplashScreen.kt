package com.example.toyproject.splash.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.toyproject.R
import com.example.toyproject.util.adaptiveIconPainterResource

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