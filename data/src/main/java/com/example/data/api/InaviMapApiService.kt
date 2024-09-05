package com.example.data.api

import com.example.data.api.model.pois.PoisSearchResponse
import com.example.data.api.model.reversegeocoding.ReverseGeoCodingSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface InaviMapApiService {
    @GET("addresses")
    suspend fun reverseGeoCoding(
        @Query("posX") posX: String,
        @Query("posY") posY: String,
        @Query("coordtype") coordType: String
    ): Response<ReverseGeoCodingSearchResponse>

    @GET("pois")
    suspend fun pois(
        @Query("poiid") poiId: String
    ): Response<PoisSearchResponse>
}