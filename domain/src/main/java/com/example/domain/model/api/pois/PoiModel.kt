package com.example.domain.model.api.pois

data class PoiModel(
    val totalCount: Int,        // 전체 검색 결과 대상 개수
    val poiInfo: List<PoiInfoModel>     // POI 정보 리스트
)