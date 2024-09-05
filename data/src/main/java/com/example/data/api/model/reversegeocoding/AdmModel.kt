package com.example.data.api.model.reversegeocoding

import com.google.gson.annotations.SerializedName

data class AdmModel(
    @SerializedName("posx") val posX: String, // X좌표
    @SerializedName("posy") val posY: String, // Y좌표
    @SerializedName("address") val address: String, // 주소
    @SerializedName("distance") val distance: Int, // 좌표와의 거리 (meter 단위). x1, y1 좌표를 기준으로 계산한 거리를 반환
    @SerializedName("admcode") val admCode: String, // 법정동 코드
    @SerializedName("jibun") val jibun: String, // 지번
    @SerializedName("roadname") val roadName: String, // 새 주소 도로명
    @SerializedName("roadjibun") val roadJibun: String, // 새 주소 지번
    @SerializedName("bldname") val bldName: String, // 건물명
    @SerializedName("postcode") val postCode: String, // 우편번호
)

fun AdmModel.toDomainModel() = com.example.domain.model.api.ReverseGeoCoding.AdmModel(
    posX = posX,
    posY = posY,
    address = address,
    distance = distance,
    admCode = admCode,
    jibun = jibun,
    roadName = roadName,
    roadJibun = roadJibun,
    bldName = bldName,
    postCode = postCode
)