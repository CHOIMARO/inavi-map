package com.example.data.api

import com.example.data.datasource.RemoteDataSource
import com.example.domain.api.ResponseState
import com.example.domain.api.repository.MapApiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MapApiRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : MapApiRepository {
    override suspend fun getReverseGeoCoding(posX: Double, posY: Double): ResponseState = withContext(ioDispatcher) {
        remoteDataSource.getReverseGeoCoding(posX, posY)
    }

    override suspend fun getDetailedSearchUsingPoiId(poiId: String): ResponseState = withContext(ioDispatcher){
        remoteDataSource.getDetailedSearchUsingPoiId(poiId)
    }
}