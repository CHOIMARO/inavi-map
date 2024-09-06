package com.example.inavi_map_compose.map.compose

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.inavi_map_compose.map.location.FusedLocationProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.inavi.mapsdk.maps.LocationProvider

@OptIn(ExperimentalPermissionsApi::class)
@Composable
public fun rememberFusedLocationSource(
    isCompassEnabled: Boolean = false,
): LocationProvider {
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )
    val context = LocalContext.current
    val locationSource = remember {
        object : FusedLocationProvider(context) {

            override fun hasPermissions(): Boolean {
                return permissionsState.allPermissionsGranted
            }

            override fun onPermissionRequest() {
                permissionsState.launchMultiplePermissionRequest()
            }
        }
    }

    val allGranted = permissionsState.allPermissionsGranted
    LaunchedEffect(allGranted) {
        if (allGranted) {
            locationSource.onPermissionGranted()
        }
    }
    LaunchedEffect(isCompassEnabled) {
        locationSource.setCompassEnabled(enabled = isCompassEnabled)
    }
    return locationSource
}
