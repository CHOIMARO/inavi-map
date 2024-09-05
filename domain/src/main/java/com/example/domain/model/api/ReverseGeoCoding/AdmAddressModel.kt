package com.example.domain.model.api.ReverseGeoCoding

data class AdmAddressModel(
    val address: String, // 주소
    val admCode: String, // 행정동 코드
    val addressCategory1: String, // 도/시
    val addressCategory2: String, // 시/군/구
    val addressCategory3: String, // 읍/면/동
    val addressCategory4: String, // 리
    val jibun: String, // 지번
    val cutAddress: String // 축약 주소
)