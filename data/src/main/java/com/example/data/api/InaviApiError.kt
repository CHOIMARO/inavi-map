package com.example.data.api

sealed class InaviApiError(val code: Int, val message: String) {
    object Success : InaviApiError(0, "성공")

    // 검색 관련 에러
    object ResultNotFound : InaviApiError(100, "검색 결과 없음")
    object ArgumentError : InaviApiError(101, "파라미터 오류")
    object InternalServerError : InaviApiError(102, "서버 오류")
    object SearchingForSecurity : InaviApiError(201, "POI 보안시설물")
    object LongitudeLatitude : InaviApiError(202, "경위도")
    object MobilePhoneNumber : InaviApiError(203, "전화번호 (Mobile)")
    object InvalidQuery : InaviApiError(204, "서버 오류")
    object PoiNotInGivenAdmin : InaviApiError(205, "결과 없음 (지역설정)")
    object PoiNotInGivenArea : InaviApiError(206, "결과 없음 (영역설정)")
    object PoiNotInGivenCategory : InaviApiError(207, "결과 없음 (분류설정)")
    object NeighborSearchOnly : InaviApiError(208, "결과 없음 (주변검색만입력)")
    object NeighborSearchNotFound : InaviApiError(209, "결과 없음 (주변 + 키워드 검색 결과 없음)")

    // 공통 에러
    object AppKeyError : InaviApiError(300, "AppKey 인증 오류")

    // 탐색 관련 에러
    object UnknownFail : InaviApiError(501, "실패(unknown)")
    object ApplySmartReNavigation : InaviApiError(502, "Smart재탐색 적용")
    object CanceledNavigationByUser : InaviApiError(503, "사용자에 의한 탐색 취소")
    object ErrorDueToChecksum : InaviApiError(504, "체크섬 오류")
    object MemoryAllocationFailure : InaviApiError(517, "메모리 할당 실패")
    object FileOpenFailure : InaviApiError(518, "파일 열기 실패")
    object FileReadFailure : InaviApiError(519, "파일 읽기 실패")
    object FileWriteFailure : InaviApiError(520, "파일 쓰기 실패")
    object SocketConnectionFailure : InaviApiError(521, "소켓 연결 실패")
    object InvalidRequestParameter : InaviApiError(532, "요청 파라미터가 유효하지 않음")
    object InvalidStartPoint : InaviApiError(533, "출발지가 선택되지 않았거나, 잘못된 출발지")
    object InvalidDestination : InaviApiError(534, "목적지가 선택되지 않았거나, 잘못된 목적지")
    object WrongStopover : InaviApiError(535, "잘못된 경유지")
    object LinkProjectionFailure : InaviApiError(536, "Link Projection")
    object ExceedingNavigationalDistance : InaviApiError(537, "탐색 가능 거리 초과(1000km, 도보 탐색 : 20km)")
    object ExceedsExpandableNodes : InaviApiError(538, "확장 가능 노드 수 초과")
    object ExpansionFailure : InaviApiError(539, "확장 실패")
    object ExpansionFailureDueToTrafficControl : InaviApiError(540, "특별한 사정이나 교통 통제로 인한 확장 실패")
    object ExpansionFailureDueToVehicleRestrictions : InaviApiError(541, "출발지 근처의 차량 높이/중량 제한으로 확장 실패")
    object ExpansionFailureDueToPartTimeCurfew : InaviApiError(542, "출발지 근처의 시간제 통행금지로 인해 확장 실패")
    object NoFerryRouteToIsland : InaviApiError(543, "목적지가 물리적 섬도로이며, 구축된 페리 항로가 없음")
    object LogicalIslandWithMultipleDestinations : InaviApiError(544, "출발 또는 목적지가 논리적(교통) 섬이며, 목적지가 2개 이상인 경우")
    object NoRequestedData : InaviApiError(545, "요청한 데이터가 없음")

    object UnknownError : InaviApiError(-1, "알 수 없는 오류")

    companion object {
        fun fromCode(code: Int): InaviApiError {
            return when (code) {
                0 -> Success
                100 -> ResultNotFound
                101 -> ArgumentError
                102 -> InternalServerError
                201 -> SearchingForSecurity
                202 -> LongitudeLatitude
                203 -> MobilePhoneNumber
                204 -> InvalidQuery
                205 -> PoiNotInGivenAdmin
                206 -> PoiNotInGivenArea
                207 -> PoiNotInGivenCategory
                208 -> NeighborSearchOnly
                209 -> NeighborSearchNotFound
                300 -> AppKeyError
                501 -> UnknownFail
                502 -> ApplySmartReNavigation
                503 -> CanceledNavigationByUser
                504 -> ErrorDueToChecksum
                517 -> MemoryAllocationFailure
                518 -> FileOpenFailure
                519 -> FileReadFailure
                520 -> FileWriteFailure
                521 -> SocketConnectionFailure
                532 -> InvalidRequestParameter
                533 -> InvalidStartPoint
                534 -> InvalidDestination
                535 -> WrongStopover
                536 -> LinkProjectionFailure
                537 -> ExceedingNavigationalDistance
                538 -> ExceedsExpandableNodes
                539 -> ExpansionFailure
                540 -> ExpansionFailureDueToTrafficControl
                541 -> ExpansionFailureDueToVehicleRestrictions
                542 -> ExpansionFailureDueToPartTimeCurfew
                543 -> NoFerryRouteToIsland
                544 -> LogicalIslandWithMultipleDestinations
                545 -> NoRequestedData
                else -> UnknownError
            }
        }
    }
}