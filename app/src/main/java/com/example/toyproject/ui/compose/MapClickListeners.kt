package com.example.toyproject.ui.compose

import android.graphics.PointF
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.inavi.mapsdk.geometry.LatLng
import com.inavi.mapsdk.maps.InaviMap

internal class MapClickListeners {
    var onMapClick: (PointF, LatLng) -> Unit by mutableStateOf({ _, _ -> })
    var onMapLongClick: (PointF, LatLng) -> Unit by mutableStateOf({ _, _ -> })
    var onMapDoubleClick: (point: PointF, coord: LatLng) -> Boolean by mutableStateOf({ _, _ -> false })
    var onMapTwoFingerTap: (point: PointF, coord: LatLng) -> Boolean by mutableStateOf({ _, _ -> false })
    var onMapLoaded: () -> Unit by mutableStateOf({})
    var onLocationChange: (Location) -> Unit by mutableStateOf({})
    var onOptionChange: () -> Unit by mutableStateOf({ })
    var onSymbolClick: (PointF, LatLng) -> Unit by mutableStateOf({ _, _ -> })
//    var onIndoorSelectionChange: (IndoorSelection?) -> Unit by mutableStateOf({})
}

/**
 * @param L InaviMap click listener type, e.g. [OnMapClickListener]
 */
internal class MapClickListenerNode<L : Any>(
    private val map: InaviMap,
    private val setter: InaviMap.(L?) -> Unit,
    private val listener: L
) : MapNode {
    override fun onAttached() = setListener(listener)
    override fun onRemoved() = setListener(null)
    override fun onCleared() = setListener(null)

    private fun setListener(listenerOrNull: L?) = map.setter(listenerOrNull)
}

/**
 * @param L InaviMap change listener type, e.g. [OnLoadListener]
 */
internal class MapChangeListenerNode<L : Any>(
    private val map: InaviMap,
    private val add: InaviMap.(L) -> Unit,
    private val remove: InaviMap.(L) -> Unit,
    private val listener: L
) : MapNode {
    override fun onAttached() = map.add(listener)
    override fun onRemoved() = Unit
    override fun onCleared() = map.remove(listener)
}

@Suppress("ComplexRedundantLet")
@Composable
internal fun MapClickListenerUpdater() {
    // The mapClickListeners container object is not allowed to ever change
    val mapClickListeners = (currentComposer.applier as MapApplier).mapClickListeners
    val inaviMap = (currentComposer.applier as MapApplier).map

    with(mapClickListeners) {
        ::onMapClick.let { callback ->
            MapClickListenerComposeNode(
                callback,
                InaviMap::setOnMapClickListener,
                InaviMap.OnMapClickListener { point, coord -> callback().invoke(point, coord) }
            )
        }

        ::onMapLongClick.let { callback ->
            MapClickListenerComposeNode(
                callback,
                InaviMap::setOnMapLongClickListener,
                InaviMap.OnMapLongClickListener { point, coord -> callback().invoke(point, coord) }
            )
        }

        ::onMapDoubleClick.let { callback ->
            MapClickListenerComposeNode(
                callback,
                InaviMap::setOnMapDoubleClickListener,
                InaviMap.OnMapDoubleClickListener { point, coord -> callback().invoke(point, coord) }
            )
        }

//        ::onMapLoaded.let { callback ->
//            MapChangeListenerComposeNode(
//                callback,
//                InaviMap::onPostMapReady,
//                NaverMap::removeOnLoadListener,
//                OnLoadListener { callback().invoke() }
//            )
//        }

//        ::onLocationChange.let { callback ->
//            MapChangeListenerComposeNode(
//                callback,
//                InaviMap::addOnLocationChangedListener
//                        InaviMap::removeOnLocationChangeListener,
//                OnLocationChangeListener { callback().invoke(it) }
//            )
//        }

//        ::onOptionChange.let { callback ->
//            MapChangeListenerComposeNode(
//                callback,
//                NaverMap::addOnOptionChangeListener,
//                NaverMap::removeOnOptionChangeListener,
//                OnOptionChangeListener { callback().invoke() }
//            )
//        }
//
//        ::onSymbolClick.let { callback ->
//            MapClickListenerComposeNode(
//                callback,
//                NaverMap::setOnSymbolClickListener,
//                OnSymbolClickListener { callback().invoke(it) }
//            )
//        }
//
//        ::onIndoorSelectionChange.let { callback ->
//            MapChangeListenerComposeNode(
//                callback,
//                NaverMap::addOnIndoorSelectionChangeListener,
//                NaverMap::removeOnIndoorSelectionChangeListener,
//                OnIndoorSelectionChangeListener { callback().invoke(it) }
//            )
//        }
    }
}

/**
 * Encapsulates the ComposeNode factory lambda as a recomposition optimization.
 *
 * @param L InaviMap click listener type, e.g. [OnMapClickListener]
 * @param callback a property reference to the callback lambda, i.e.
 * invoking it returns the callback lambda
 * @param setter a reference to a NaverMap setter method, e.g. `setOnMapClickListener()`
 * @param listener must include a call to `callback()` inside the listener
 * to use the most up-to-date recomposed version of the callback lambda;
 * However, the resulting callback reference might actually be null due to races;
 * the caller must guard against this case.
 *
 */
@Composable
@NonRestartableComposable
private fun <L : Any> MapClickListenerComposeNode(
    callback: () -> Any?,
    setter: InaviMap.(L?) -> Unit,
    listener: L
) {
    val mapApplier = currentComposer.applier as MapApplier

    MapClickListenerComposeNode(callback) { MapClickListenerNode(mapApplier.map, setter, listener) }
}

@Composable
private fun MapClickListenerComposeNode(
    callback: () -> Any?,
    factory: () -> MapClickListenerNode<*>
) {
    if (callback() != null) ComposeNode<MapClickListenerNode<*>, MapApplier>(factory) {}
}

@Composable
@NonRestartableComposable
private fun <L : Any> MapChangeListenerComposeNode(
    callback: () -> Any?,
    add: InaviMap.(L) -> Unit,
    remove: InaviMap.(L) -> Unit,
    listener: L
) {
    val mapApplier = currentComposer.applier as MapApplier

    MapChangeListenerComposeNode(callback) {
        MapChangeListenerNode(mapApplier.map, add, remove, listener)
    }
}

@Composable
private fun MapChangeListenerComposeNode(
    callback: () -> Any?,
    factory: () -> MapChangeListenerNode<*>
) {
    if (callback() != null) ComposeNode<MapChangeListenerNode<*>, MapApplier>(factory) {}
}
