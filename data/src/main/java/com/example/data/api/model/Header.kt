package com.example.data.api.model

import com.google.gson.annotations.SerializedName


data class Header(
    @SerializedName("isSuccessful")
    val isSuccessful: Boolean,
    @SerializedName("resultCode")
    val resultCode: Int,
    @SerializedName("resultMessage")
    val resultMessage: String
)

fun Header.toDomainModel(): com.example.domain.model.api.Header {
    return com.example.domain.model.api.Header(
        isSuccessful = isSuccessful,
        resultCode = resultCode,
        resultMessage = resultMessage
    )
}