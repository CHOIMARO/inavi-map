package com.example.domain.model.api.ReverseGeoCoding


data class LocationModel(
    val hasAdmAddress: Boolean, // 행정동 주소 반환 여부
    val adm: AdmModel, // 기본(법정동) 주소 정보
    val admAddress: AdmAddressModel?, // 행정동 주소 정보
    val legalAddress: LegalAddressModel // 법정동 주소 정보
)