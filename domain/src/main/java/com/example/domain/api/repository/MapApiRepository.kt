package com.example.domain.api.repository

import com.example.domain.api.ResponseState

interface MapApiRepository {
    suspend fun getReverseGeoCoding(posX: Double, posY: Double): ResponseState
    suspend fun getDetailedSearchUsingPoiId(poiId: String): ResponseState
}