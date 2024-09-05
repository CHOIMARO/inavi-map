package com.example.toyproject.ui.compose

import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.graphics.PointF
import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.MapView
import com.inavi.mapsdk.geometry.LatLng
import com.inavi.mapsdk.maps.InaviMap
import com.inavi.mapsdk.maps.InvMapView
import com.inavi.mapsdk.maps.LocationProvider
import kotlinx.coroutines.awaitCancellation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
public fun InaviMapCompose(
    modifier: Modifier = Modifier,
    mapUiSettings: MapUiSettings = DefaultMapUiSettings,
    mapProperties: MapProperties = DefaultMapProperties,
    locationProvider: LocationProvider? = null,
    onMapClick: (PointF, LatLng) -> Unit = { _, _ -> },
    onMapLongClick: (PointF, LatLng) -> Unit = { _, _ -> },
    onMapDoubleClick: (point: PointF, coord: LatLng) -> Boolean = { _, _ -> false },
    content: @Composable () -> Unit = {},
    ) {
    val context = LocalContext.current
    val mapView = remember { InvMapView(context) }

    AndroidView(factory = { mapView }, modifier = modifier.fillMaxSize())
    MapLifecycle(mapView = mapView)

    val mapClickListeners = remember { MapClickListeners() }.also {
        it.onMapClick = onMapClick
        it.onMapLongClick = onMapLongClick
        it.onMapDoubleClick = onMapDoubleClick
    }

    val currentLocationProvider by rememberUpdatedState(locationProvider)
    val currentUiSettings by rememberUpdatedState(mapUiSettings)
    val currentMapProperties by rememberUpdatedState(mapProperties)
    val parentComposition = rememberCompositionContext()
    val currentContent by rememberUpdatedState(content)

    LaunchedEffect(Unit) {
        disposingComposition {
            mapView.newComposition(parentComposition, mapClickListeners) {
                MapUpdater(
                    locationProvider = currentLocationProvider,
                    mapProperties = currentMapProperties,
                    mapUiSettings = currentUiSettings
                )

                MapClickListenerUpdater()
                CompositionLocalProvider(
                    content = currentContent
                )
            }
        }
    }
}

private suspend inline fun disposingComposition(factory: () -> Composition) {
    val composition = factory()
    try {
        awaitCancellation()
    } finally {
        composition.dispose()
    }
}

private suspend inline fun InvMapView.newComposition(
    parent: CompositionContext,
    mapClickListeners: MapClickListeners,
    noinline content: @Composable () -> Unit,
): Composition {
    val map = awaitMap() //getMapAsync를 통해 onMapReady가 되면 callback 실행
    return Composition(
        MapApplier(map, mapClickListeners), parent
    ).apply {
        setContent(content)
    }
}

/**
 * A suspending function that provides an instance of [InaviMapCompose] from this [MapView]. This is
 * an alternative to [MapView.getMapAsync] by using coroutines to obtain the [InaviMapCompose].
 *
 * @return the [InaviMapCompose] instance
 */
private suspend inline fun InvMapView.awaitMap(): InaviMap {
    return suspendCoroutine { continuation ->
        getMapAsync {
            continuation.resume(it)
        }
    }
}

/**
 * Registers lifecycle observers to the local [InvMapView].
 */
@Composable
private fun MapLifecycle(mapView: InvMapView) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val previousState = remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
    val savedInstanceState = rememberSavedInstanceState()
    DisposableEffect(context, lifecycle, mapView, savedInstanceState) {
        val mapLifecycleObserver = mapView.lifecycleObserver(
            savedInstanceState.takeUnless { it.isEmpty },
            previousState,
        )
        val callbacks = mapView.componentCallbacks()

        lifecycle.addObserver(mapLifecycleObserver)
        context.registerComponentCallbacks(callbacks)

        onDispose {
            mapView.onSaveInstanceState(savedInstanceState)
            lifecycle.removeObserver(mapLifecycleObserver)
            context.unregisterComponentCallbacks(callbacks)

            // dispose 시점에 Lifecycle.Event가 끝까지 진행되지 않아 발생되는
            // MapView Memory Leak 수정합니다.
            when (previousState.value) {
                Lifecycle.Event.ON_CREATE, Lifecycle.Event.ON_STOP -> {
                    mapView.onDestroy()
                }
                Lifecycle.Event.ON_START, Lifecycle.Event.ON_PAUSE -> {
                    mapView.onStop()
                    mapView.onDestroy()
                }
                Lifecycle.Event.ON_RESUME -> {
                    mapView.onPause()
                    mapView.onStop()
                    mapView.onDestroy()
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun rememberSavedInstanceState(): Bundle {
    return rememberSaveable { Bundle() }
}

private fun InvMapView.lifecycleObserver(
    savedInstanceState: Bundle?,
    previousState: MutableState<Lifecycle.Event>,
): LifecycleEventObserver {
    return LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> this.onCreate(savedInstanceState)
            Lifecycle.Event.ON_START -> this.onStart()
            Lifecycle.Event.ON_RESUME -> this.onResume()
            Lifecycle.Event.ON_PAUSE -> this.onPause()
            Lifecycle.Event.ON_STOP -> this.onStop()
            Lifecycle.Event.ON_DESTROY -> this.onDestroy()
            else -> throw IllegalStateException()
        }
        previousState.value = event
    }
}

private fun InvMapView.componentCallbacks(): ComponentCallbacks {
    return object : ComponentCallbacks {
        override fun onConfigurationChanged(config: Configuration) {}

        override fun onLowMemory() {
            this@componentCallbacks.onLowMemory()
        }
    }
}