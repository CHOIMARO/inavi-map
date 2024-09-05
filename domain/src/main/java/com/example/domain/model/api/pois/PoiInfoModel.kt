package com.example.domain.model.api.pois

/**
 * 개별 POI 정보를 나타내는 데이터 클래스
 */
data class PoiInfoModel(
    val poiId: Int,             // POI ID
    val dpX: String,            // display X좌표 (WGS84의 경우 longitude)
    val dpY: String,            // display Y좌표 (WGS84의 경우 latitude)
    val rpX: String,            // 탐색 X좌표 (WGS84의 경우 longitude)
    val rpY: String,            // 탐색 Y좌표 (WGS84의 경우 latitude)
    val name1: String,          // 정식 명칭
    val name2: String,          // 축약 명칭
    val name3: String,          // 확장 명칭1
    val name4: String,          // 확장 명칭2
    val admCode: String,        // 법정동 코드
    val address: String,        // 주소
    val jibun: String,          // 지번
    val roadName: String,       // 새 주소 도로명
    val roadJibun: String,      // 새 주소 지번
    val detailAddress: String,  // 상세 주소
    val cateCode: String,       // 분류 코드
    val cateName: String,       // 분류 명칭
    val fullAddress: String,    // 전체 주소 (행정주소+지번+상세주소)
    val zip: String,            // 우편번호
    val homepage: String?,        // 홈페이지 url
    val email: String?,          // email
    val howToGo: String?,        // 교통편
    val tel1: String?,           // 전화번호1
    val tel2: String?,           // 전화번호2
    val fax1: String?,           // 팩스번호1
    val fax2: String?,           // 팩스번호2
    val detailCount: Int,      // 분류 상세 항목 갯수
    val etcCount: Int,         // 분류 기타 항목 갯수
    val detailInfo: List<DetailInfoModel>?,  // 분류 상세 정보 리스트
    val etcInfo: List<EtcInfoModel>?  // 분류 기타 정보 리스트
)