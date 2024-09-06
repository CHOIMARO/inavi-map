package com.example.toyproject.home.viewmodel

import android.graphics.RectF
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.api.ResponseState
import com.example.domain.api.usecase.GetDetailedSearchUsingPoiIdUseCase
import com.example.domain.api.usecase.GetReverseGeoCodingUseCase
import com.example.domain.map.usecase.SetSdkAppKeyUseCase
import com.example.domain.model.api.ReverseGeoCoding.LocationModel
import com.example.domain.model.api.pois.PoiModel
import com.example.inavi_map_compose.map.compose.InvMarkerState
import com.example.toyproject.home.model.ClickType
import com.example.toyproject.home.model.MarkerInformationModel
import com.example.toyproject.home.model.ProcessedMarkerModel
import com.example.toyproject.home.model.ReverseGeoCodingLocationModel
import com.example.toyproject.util.DpToPx
import com.inavi.mapsdk.maps.InaviMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val setSdkAppKeyUseCase: SetSdkAppKeyUseCase,
    private val getReverseGeoCodingUseCase: GetReverseGeoCodingUseCase,
    private val getDetailedSearchUsingPoiIdUseCase: GetDetailedSearchUsingPoiIdUseCase
): ViewModel() {
    private val _latestClickType = MutableStateFlow<ClickType?>(null)
    val latestClickType = _latestClickType.asStateFlow()

    private val _reverseGeoCodingData = MutableStateFlow<ReverseGeoCodingLocationModel?>(null)
    val reverseGeoCodingData = _reverseGeoCodingData.asStateFlow()

    private val _processedMarkerModelState = MutableStateFlow<ProcessedMarkerModel?>(null)
    val processedMarkerModel = _processedMarkerModelState.asStateFlow()

    private val _clickedMarkerState = MutableStateFlow<InvMarkerState?>(null)
    val clickedMarkerState = _clickedMarkerState.asStateFlow()

    private val _detailedSearchData = MutableStateFlow<MarkerInformationModel?>(null)
    val detailedSearchData = _detailedSearchData.asStateFlow()

    fun setSdkAppKey() = viewModelScope.launch {
        setSdkAppKeyUseCase()
    }
    fun getReverseGeoCoding(posX: Double, posY: Double) = viewModelScope.launch {
        val apiResult = getReverseGeoCodingUseCase(posX, posY)
        when(apiResult) {
            is ResponseState.Success<*> -> {
                val data = apiResult.data as? LocationModel
                data?.let {
                    setReverseGeoCodingData(data)
                }
            }
            is ResponseState.Fail -> {
                Log.e(">>>>>", "Fail")
            }
            is ResponseState.Loading -> {

            }
            ResponseState.Init -> {

            }

            else -> {}
        }
    }
    fun getDetailedSearchData(poiId: String) = viewModelScope.launch {
        val apiResult = getDetailedSearchUsingPoiIdUseCase(poiId)
        when(apiResult) {
            is ResponseState.Success<*> -> {
                val data = apiResult.data as PoiModel
                _detailedSearchData.value = MarkerInformationModel(
                    clickType = processedMarkerModel.value?.clickType,
                    invMarkerState = processedMarkerModel.value?.invMarkerState,
                    address = data.poiInfo.first().address + data.poiInfo.first().jibun,
                    poiId = poiId,
                    title = processedMarkerModel.value?.title
                )
            }
            is ResponseState.Fail -> {
                Log.e(">>>>>", "Fail")
            }
            is ResponseState.Loading -> {

            }
            ResponseState.Init -> {

            }
        }
    }

    fun setClickedMarkerState(invMarkerState: InvMarkerState?, clickType: ClickType) {
        _latestClickType.value = clickType
        _clickedMarkerState.value = invMarkerState
    }
    fun setReverseGeoCodingData(locationModel: LocationModel) {
        _reverseGeoCodingData.value = ReverseGeoCodingLocationModel(
            invMarkerState = processedMarkerModel.value?.invMarkerState,
            locationModel = locationModel
        )
    }
    fun setProcessedMarkerState(map: InaviMap) {
        clickedMarkerState.value.let { marker ->
            when(latestClickType.value) {
                ClickType.CLICK -> {
                    marker?.point?.let { pointF ->
                        val touchSize = 20.DpToPx()
                        val touchRect = RectF(
                            pointF.x - touchSize / 2,
                            pointF.y - touchSize / 2,
                            pointF.x + touchSize / 2,
                            pointF.y + touchSize / 2,
                        )
                        val pois = map.pickPois(touchRect)
                        if (pois.isNotEmpty()) {
                            setProcessedMarkerModelState(latestClickType.value!!, InvMarkerState(position = pois.first().position), pois.first().poiId.toString(), pois.first().name)
                        } else {
                            setProcessedMarkerModelState(latestClickType.value!!, InvMarkerState(position = marker.position), null, null)
                        }
                    }
                }
                ClickType.LONG_CLICK -> {
                    Log.e(">>>>>", "Long Click")
                    setProcessedMarkerModelState(latestClickType.value!!, InvMarkerState(position = marker?.position), null, null)
                }
                ClickType.DOUBLE_CLICK -> {

                }
                null -> {}
            }
        }
    }

    private fun setProcessedMarkerModelState(clickType: ClickType, invMarkerState: InvMarkerState?, poiId: String?, title: String?) {
        _processedMarkerModelState.value = ProcessedMarkerModel(
            clickType = clickType,
            invMarkerState = invMarkerState,
            poiId = poiId,
            address = null,
            title = title
        )
    }

    fun setDetailedSearchData(markerInformationModel: MarkerInformationModel?) {
        _detailedSearchData.value = markerInformationModel
    }
}