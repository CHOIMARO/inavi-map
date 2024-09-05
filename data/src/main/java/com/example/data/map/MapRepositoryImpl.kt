package com.example.data.map

import com.example.data.datasource.RemoteDataSource
import com.example.domain.map.repository.MapRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : MapRepository {
    override suspend fun setSdkAppKey() {
        remoteDataSource.setSdkAppKey()
    }
}