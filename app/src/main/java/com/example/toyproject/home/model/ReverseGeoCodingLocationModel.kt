package com.example.toyproject.home.model

import com.example.domain.model.api.ReverseGeoCoding.LocationModel
import com.example.inavi_map_compose.map.compose.InvMarkerState

data class ReverseGeoCodingLocationModel(
    val invMarkerState: InvMarkerState?,
    val locationModel: LocationModel?
)