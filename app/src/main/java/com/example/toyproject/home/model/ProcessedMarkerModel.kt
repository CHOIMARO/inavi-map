package com.example.toyproject.home.model

import com.example.inavi_map_compose.map.compose.InvMarkerState

data class ProcessedMarkerModel(
    val clickType: ClickType?,
    val invMarkerState: InvMarkerState?,
    val title: String?,
    val address: String?,
    val poiId: String?
)

enum class ClickType {
    CLICK,
    LONG_CLICK,
    DOUBLE_CLICK
}