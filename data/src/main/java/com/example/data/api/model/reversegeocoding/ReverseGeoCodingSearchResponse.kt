package com.example.data.api.model.reversegeocoding

import com.example.domain.model.api.Header
import com.google.gson.annotations.SerializedName

data class ReverseGeoCodingSearchResponse(
    @SerializedName("header")
    val header: Header,
    @SerializedName("location")
    val location: LocationModel
)

fun ReverseGeoCodingSearchResponse.toDomainModel() = com.example.domain.model.api.ReverseGeoCoding.ReverseGeoCodingSearchResponse(
    header = header, // 이미 도메인 모델을 사용하고 있으므로 변환 불필요
    location = location.toDomainModel()
)