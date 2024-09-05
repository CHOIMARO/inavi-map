package com.example.data.api.model.pois

import com.google.gson.annotations.SerializedName

/**
 * 개별 POI 정보를 나타내는 데이터 클래스
 */
data class PoiInfoModel(
    @SerializedName("poiid") val poiId: Int,             // POI ID
    @SerializedName("dpx") val dpX: String,              // display X좌표 (WGS84의 경우 longitude)
    @SerializedName("dpy") val dpY: String,              // display Y좌표 (WGS84의 경우 latitude)
    @SerializedName("rpx") val rpX: String,              // 탐색 X좌표 (WGS84의 경우 longitude)
    @SerializedName("rpy") val rpY: String,              // 탐색 Y좌표 (WGS84의 경우 latitude)
    @SerializedName("name1") val name1: String,          // 정식 명칭
    @SerializedName("name2") val name2: String,          // 축약 명칭
    @SerializedName("name3") val name3: String,          // 확장 명칭1
    @SerializedName("name4") val name4: String,          // 확장 명칭2
    @SerializedName("admcode") val admCode: String,      // 법정동 코드
    @SerializedName("address") val address: String,      // 주소
    @SerializedName("jibun") val jibun: String,          // 지번
    @SerializedName("roadname") val roadName: String,    // 새 주소 도로명
    @SerializedName("roadjibun") val roadJibun: String,  // 새 주소 지번
    @SerializedName("detailaddress") val detailAddress: String,  // 상세 주소
    @SerializedName("catecode") val cateCode: String,    // 분류 코드
    @SerializedName("catename") val cateName: String,    // 분류 명칭
    @SerializedName("fulladdress") val fullAddress: String,  // 전체 주소 (행정주소+지번+상세주소)
    @SerializedName("zip") val zip: String,              // 우편번호
    @SerializedName("hompage") val homepage: String?,     // 홈페이지 url
    @SerializedName("email") val email: String?,          // email
    @SerializedName("howtogo") val howToGo: String?,      // 교통편
    @SerializedName("tel1") val tel1: String?,            // 전화번호1
    @SerializedName("tel2") val tel2: String?,            // 전화번호2
    @SerializedName("fax1") val fax1: String?,            // 팩스번호1
    @SerializedName("fax2") val fax2: String?,            // 팩스번호2
    @SerializedName("detail_count") val detailCount: Int,  // 분류 상세 항목 갯수
    @SerializedName("etc_count") val etcCount: Int,      // 분류 기타 항목 갯수
    @SerializedName("detailinfo") val detailInfo: List<DetailInfoModel>?,  // 분류 상세 정보 리스트
    @SerializedName("etcinfo") val etcInfo: List<EtcInfoModel>?  // 분류 기타 정보 리스트
)

fun PoiInfoModel.toDomainModel() = com.example.domain.model.api.pois.PoiInfoModel(
    poiId = poiId,
    dpX = dpX,
    dpY = dpY,
    rpX = rpX,
    rpY = rpY,
    name1 = name1,
    name2 = name2,
    name3 = name3,
    name4 = name4,
    admCode = admCode,
    address = address,
    jibun = jibun,
    roadName = roadName,
    roadJibun = roadJibun,
    detailAddress = detailAddress,
    cateCode = cateCode,
    cateName = cateName,
    fullAddress = fullAddress,
    zip = zip,
    homepage = homepage,
    email = email,
    howToGo = howToGo,
    tel1 = tel1,
    tel2 = tel2,
    fax1 = fax1,
    fax2 = fax2,
    detailCount = detailCount,
    etcCount = etcCount,
    detailInfo = detailInfo?.map { it.toDomainModel() } ?: null,
    etcInfo = etcInfo?.map { it.toDomainModel() } ?: null
)