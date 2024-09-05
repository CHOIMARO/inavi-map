package com.example.data.api.model.pois

import com.google.gson.annotations.SerializedName

/**
 * POI의 상세 정보를 나타내는 데이터 클래스
 */
data class DetailInfoModel(
    @SerializedName("name") val name: String,   // 분류 상세 항목 설명
    @SerializedName("value") val value: String  // 분류 상세 항목 내용
)

fun DetailInfoModel.toDomainModel() = com.example.domain.model.api.pois.DetailInfoModel(
    name = name,
    value = value
)