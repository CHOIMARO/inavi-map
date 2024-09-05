package com.example.domain.api.usecase

import com.example.domain.api.ResponseState
import com.example.domain.api.repository.MapApiRepository

class GetDetailedSearchUsingPoiIdUseCase(private val inaviMapApiRepository: MapApiRepository){
    suspend operator fun invoke(poiId: String): ResponseState {
        return inaviMapApiRepository.getDetailedSearchUsingPoiId(poiId)
    }
}