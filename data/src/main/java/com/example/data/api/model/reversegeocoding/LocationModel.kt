package com.example.data.api.model.reversegeocoding

import com.google.gson.annotations.SerializedName

data class LocationModel(
    @SerializedName("hasAdmAddress") val hasAdmAddress: Boolean, // 행정동 주소 반환 여부
    @SerializedName("adm") val adm: AdmModel, // 기본(법정동) 주소 정보
    @SerializedName("adm_address") val admAddress: AdmAddressModel?, // 행정동 주소 정보
    @SerializedName("legal_address") val legalAddress: LegalAddressModel // 법정동 주소 정보

)

fun LocationModel.toDomainModel() = com.example.domain.model.api.ReverseGeoCoding.LocationModel(
    hasAdmAddress = hasAdmAddress,
    adm = adm.toDomainModel(),
    admAddress = admAddress?.toDomainModel(),
    legalAddress = legalAddress.toDomainModel()
)