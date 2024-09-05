package com.example.toyproject.home.model

import InvMarkerState
import com.example.domain.model.api.ReverseGeoCoding.LocationModel

data class ReverseGeoCodingLocationModel(
    val invMarkerState: InvMarkerState?,
    val locationModel: LocationModel?
)