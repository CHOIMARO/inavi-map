package com.example.data.datasource

import android.content.Context
import android.util.Log
import com.example.data.api.InaviApiError
import com.example.data.api.InaviMapApiService
import com.example.data.api.model.pois.toDomainModel
import com.example.data.api.model.reversegeocoding.toDomainModel
import com.example.data.sdk.InaviService
import com.example.domain.ContextProvider
import com.example.domain.api.ResponseState
import com.example.domain.model.api.ReverseGeoCoding.LocationModel
import com.inavi.mapsdk.geometry.LatLng
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val inaviService: InaviService,
    private val inaviMapApiService: InaviMapApiService,
    private val retrofit: Retrofit,
    private val contextProvider: ContextProvider
) {
    suspend fun setSdkAppKey() {
        inaviService.initialize(contextProvider.getContext() as Context)
        inaviService.setAuthFailureCallback { errCode, msg ->
            Log.e(">>>>>", "Failed ErrCode and Message : $errCode, $msg")
        }
        inaviService.setAuthSuccessCallback { mapStyles ->
            Log.e(">>>>>", "Success")
        }
    }

    suspend fun getReverseGeoCoding(posX: Double, posY: Double): ResponseState {
        try {
            inaviMapApiService.reverseGeoCoding(
                posX = posX.toString(),
                posY = posY.toString(),
                coordType = "1"
            ).let { response ->
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        when (val error = InaviApiError.fromCode(body.header.resultCode)) {
                            is InaviApiError.Success -> {
                                body.location.let { locationModel ->
                                    return ResponseState.Success(locationModel.toDomainModel())
                                }
                            }

                            is InaviApiError.ResultNotFound -> return ResponseState.Fail("검색 결과를 찾을 수 없습니다.")
                            is InaviApiError.ArgumentError -> return ResponseState.Fail("잘못된 파라미터: ${error.message}")
                            is InaviApiError.InternalServerError -> return ResponseState.Fail("서버 내부 오류: ${error.message}")
                            is InaviApiError.AppKeyError -> return ResponseState.Fail("AppKey 인증 오류: ${error.message}")
                            // 필요에 따라 다른 에러 케이스 추가
                            else -> return ResponseState.Fail("API 오류: ${error.code} - ${error.message}")
                        }
                    } ?: return ResponseState.Fail("응답 본문이 비어 있습니다.")
                } else {
                    val errorBody = response.errorBody()?.string() ?: "알 수 없는 오류"
                    return ResponseState.Fail("HTTP 오류: ${response.code()}, $errorBody")

                }
            }
        } catch (e: Exception) {
            return ResponseState.Fail(e.message ?: "알 수 없는 오류가 발생했습니다.")
        }
    }

    suspend fun getDetailedSearchUsingPoiId(poiId: String): ResponseState {
        try {
            inaviMapApiService.pois(poiId).let { response ->
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        when (val error = InaviApiError.fromCode(body.header.resultCode)) {
                            is InaviApiError.Success -> {
                                body.poi.let { poiModel ->
                                    return ResponseState.Success(poiModel.toDomainModel())
                                }
                            }

                            is InaviApiError.ResultNotFound -> return ResponseState.Fail("검색 결과를 찾을 수 없습니다.")
                            is InaviApiError.ArgumentError -> return ResponseState.Fail("잘못된 파라미터: ${error.message}")
                            is InaviApiError.InternalServerError -> return ResponseState.Fail("서버 내부 오류: ${error.message}")
                            is InaviApiError.AppKeyError -> return ResponseState.Fail("AppKey 인증 오류: ${error.message}")
                            // 필요에 따라 다른 에러 케이스 추가
                            else -> return ResponseState.Fail("API 오류: ${error.code} - ${error.message}")
                        }
                    } ?: return ResponseState.Fail("응답 본문이 비어 있습니다.")
                } else {
                    val errorBody = response.errorBody()?.string() ?: "알 수 없는 오류"
                    return ResponseState.Fail("HTTP 오류: ${response.code()}, $errorBody")

                }
            }
        } catch (e: Exception) {
            return ResponseState.Fail(e.message ?: "알 수 없는 오류가 발생했습니다.")
        }
    }
}