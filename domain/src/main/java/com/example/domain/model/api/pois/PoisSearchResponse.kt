package com.example.domain.model.api.pois

import com.example.domain.model.api.Header

data class PoisSearchResponse(
    val header: Header,
    val poi: PoiModel
)