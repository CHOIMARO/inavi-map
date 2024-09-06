package com.example.toyproject.splash.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.toyproject.home.view.activity.MainActivity
import com.example.toyproject.splash.view.activity.ui.theme.ToyProjectTheme
import com.example.toyproject.splash.view.screen.ShowSplashScreen
import com.example.toyproject.util.LocationPermissionRequest
import kotlinx.coroutines.delay

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