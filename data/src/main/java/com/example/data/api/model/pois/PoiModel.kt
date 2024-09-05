package com.example.data.api.model.pois

import com.google.gson.annotations.SerializedName

data class PoiModel(
    @SerializedName("totalcount") val totalCount: Int,        // 전체 검색 결과 대상 개수
    @SerializedName("poiinfo") val poiInfo: List<PoiInfoModel>     // POI 정보 리스트
)

fun PoiModel.toDomainModel() = com.example.domain.model.api.pois.PoiModel(
    totalCount = totalCount,
    poiInfo = poiInfo.map { it.toDomainModel() }
)