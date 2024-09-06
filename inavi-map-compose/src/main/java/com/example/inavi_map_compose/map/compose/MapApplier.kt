package com.example.inavi_map_compose.map.compose

import androidx.compose.runtime.AbstractApplier
import com.inavi.mapsdk.maps.InaviMap
import com.inavi.mapsdk.style.shapes.InvMarker

interface MapNode {
    fun onAttached() {}
    fun onRemoved() {}
    fun onCleared() {}
}

private object MapNodeRoot : MapNode

class MapApplier(
    val map: InaviMap,
    val mapClickListeners: MapClickListeners
) : AbstractApplier<MapNode>(MapNodeRoot) {

    private val decorations = mutableListOf<MapNode>()

    override fun onClear() {
        decorations.forEach { it.onCleared() }
        decorations.clear()
    }

    override fun insertBottomUp(index: Int, instance: MapNode) {
        decorations.add(index, instance)
        instance.onAttached()
    }

    override fun insertTopDown(index: Int, instance: MapNode) {
        // insertBottomUp is preferred
    }

    override fun move(from: Int, to: Int, count: Int) {
        decorations.move(from, to, count)
    }

    override fun remove(index: Int, count: Int) {
        repeat(count) {
            decorations[index + it].onRemoved()
        }
        decorations.remove(index, count)
    }

    internal fun nodeForMarker(
        marker: InvMarker,
    ): InvMarkerNode? {
        return decorations.firstOrNull {
            it is InvMarkerNode && it.overlay == marker
        } as? InvMarkerNode
    }

}