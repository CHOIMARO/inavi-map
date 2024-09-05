package com.example.domain.model.api.ReverseGeoCoding

data class AdmModel(
    val posX: String, // X좌표
    val posY: String, // Y좌표
    val address: String, // 주소
    val distance: Int, // 좌표와의 거리 (meter 단위). x1, y1 좌표를 기준으로 계산한 거리를 반환
    val admCode: String, // 법정동 코드
    val jibun: String, // 지번
    val roadName: String, // 새 주소 도로명
    val roadJibun: String, // 새 주소 지번
    val bldName: String, // 건물명
    val postCode: String, // 우편번호
)