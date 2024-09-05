package com.example.toyproject.home.model

import InvMarkerState

data class MarkerInformationModel(
    val clickType: ClickType?,
    val invMarkerState: InvMarkerState?,
    val title: String?,
    val address: String?,
    val poiId: String?
)