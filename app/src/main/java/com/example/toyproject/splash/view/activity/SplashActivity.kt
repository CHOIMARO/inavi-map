package com.example.toyproject.splash.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AdaptiveIconDrawable
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.modifier.ModifierLocal
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.toyproject.R
import com.example.toyproject.home.view.activity.MainActivity
import com.example.toyproject.splash.view.activity.ui.theme.ToyProjectTheme
import com.example.toyproject.splash.view.screen.ShowSplashScreen
import com.example.toyproject.ui.compose.LocationPermissionRequest
import com.example.toyproject.ui.compose.adaptiveIconPainterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToyProjectTheme {
                var permissionGranted by remember { mutableStateOf(false) }
                ShowSplashScreen()
                LocationPermissionRequest(
                    onPermissionGranted = {
                        permissionGranted = true
                    },
                    onPermissionDenied = {
                        finishAffinity()
                    }
                )

                LaunchedEffect(permissionGranted) {
                    if (permissionGranted) {
                        delay(2000)
                        Intent(this@SplashActivity, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }.let { intent ->
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}