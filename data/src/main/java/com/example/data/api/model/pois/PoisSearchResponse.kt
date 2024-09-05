package com.example.data.api.model.pois

import com.example.data.api.model.reversegeocoding.LocationModel
import com.example.domain.model.api.Header
import com.google.gson.annotations.SerializedName

data class PoisSearchResponse(
    @SerializedName("header")
    val header: Header,
    @SerializedName("poi")
    val poi: PoiModel
)