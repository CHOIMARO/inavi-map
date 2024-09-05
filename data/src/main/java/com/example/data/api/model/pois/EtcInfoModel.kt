package com.example.data.api.model.pois

import com.google.gson.annotations.SerializedName

/**
 * POI의 기타 정보를 나타내는 데이터 클래스
 */
data class EtcInfoModel(
    @SerializedName("name") val name: String,   // 분류 기타 항목 설명
    @SerializedName("value") val value: String  // 분류 기타 항목 내용
)

fun EtcInfoModel.toDomainModel() = com.example.domain.model.api.pois.EtcInfoModel(
    name = name,
    value = value
)