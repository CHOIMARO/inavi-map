package com.example.domain.model.api.ReverseGeoCoding

import com.example.domain.model.api.Header

data class ReverseGeoCodingSearchResponse(
    val header: Header,
    val location: LocationModel
)