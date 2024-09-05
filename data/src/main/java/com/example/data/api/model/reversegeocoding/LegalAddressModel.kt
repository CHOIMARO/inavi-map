package com.example.data.api.model.reversegeocoding

import com.google.gson.annotations.SerializedName

data class LegalAddressModel(
    @SerializedName("address") val address: String, // 주소
    @SerializedName("admcode") val admCode: String, // 법정동 코드
    @SerializedName("address_category1") val addressCategory1: String, // 도/시
    @SerializedName("address_category2") val addressCategory2: String, // 시/군/구
    @SerializedName("address_category3") val addressCategory3: String, // 읍/면/동
    @SerializedName("address_category4") val addressCategory4: String, // 리
    @SerializedName("jibun") val jibun: String, // 지번
    @SerializedName("cut_address") val cutAddress: String // 축약 주소
)

fun LegalAddressModel.toDomainModel() = com.example.domain.model.api.ReverseGeoCoding.LegalAddressModel(
    address = address,
    admCode = admCode,
    addressCategory1 = addressCategory1,
    addressCategory2 = addressCategory2,
    addressCategory3 = addressCategory3,
    addressCategory4 = addressCategory4,
    jibun = jibun,
    cutAddress = cutAddress
)