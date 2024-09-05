package com.example.domain.model.api.pois


/**
 * POI의 기타 정보를 나타내는 데이터 클래스
 */
data class EtcInfoModel(
    val name: String,   // 분류 기타 항목 설명
    val value: String  // 분류 기타 항목 내용
)
