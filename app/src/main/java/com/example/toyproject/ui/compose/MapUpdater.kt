package com.example.toyproject.ui.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.inavi.mapsdk.maps.InaviMap
import com.inavi.mapsdk.maps.LocationProvider
import java.util.Locale

internal class MapPropertiesNode(
    val map: InaviMap,
    var density: Density,
    var layoutDirection: LayoutDirection,
) : MapNode {

    override fun onAttached() {

    }

    override fun onRemoved() {

    }

    override fun onCleared() {

    }
}

internal val NoPadding = PaddingValues()

/**
 * Used to keep the primary map properties up to date. This should never leave the map composition.
 */
@Suppress("NOTHING_TO_INLINE")
@Composable
internal inline fun MapUpdater(
    contentPadding: PaddingValues = NoPadding,
    locationProvider: LocationProvider?,
    locale: Locale? = null,
    mapProperties: MapProperties,
    mapUiSettings: MapUiSettings,
) {
    val map = (currentComposer.applier as MapApplier).map
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    ComposeNode<MapPropertiesNode, MapApplier>(
        factory = {
            MapPropertiesNode(
                map = map,
                density = density,
                layoutDirection = layoutDirection,
            )
        }
    ) {
        // The node holds density and layoutDirection so that the updater blocks can be
        // non-capturing, allowing the compiler to turn them into singletons
        update(density) { this.density = it }
        update(layoutDirection) { this.layoutDirection = it }

        set(mapProperties.minZoom) { map.minZoom = it }
        set(mapProperties.maxZoom) { map.maxZoom = it }
        set(mapProperties.maxTilt) { map.maxTilt = it }


        set(mapUiSettings.isScrollGesturesEnabled) { map.uiSettings.isScrollGesturesEnabled = it }
        set(mapUiSettings.isZoomGesturesEnabled) { map.uiSettings.isZoomGesturesEnabled = it }
        set(mapUiSettings.isTiltGesturesEnabled) { map.uiSettings.isTiltGesturesEnabled = it }
        set(mapUiSettings.isRotateGesturesEnabled) { map.uiSettings.isRotateGesturesEnabled = it }
        set(mapUiSettings.isCompassEnabled) { map.uiSettings.isCompassVisible = it }
        set(mapUiSettings.isScaleBarEnabled) { map.uiSettings.isScaleBarVisible = it }
        set(mapUiSettings.isZoomControlEnabled) { map.uiSettings.isZoomControlVisible = it }

        set(mapUiSettings.isLocationButtonEnabled) { map.uiSettings.isLocationButtonVisible = it }
        set(mapUiSettings.isLogoClickEnabled) { map.uiSettings.isLogoClickEnabled = it }
        set(mapUiSettings.logoGravity) { map.uiSettings.logoGravity = it }

        set(mapProperties.mapType) { map.mapType = it.value }

        set(locationProvider) { map.locationProvider = it }
        set(mapProperties.userTrackingMode) { map.userTrackingMode = it.value }
    }
}
