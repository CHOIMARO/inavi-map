package com.example.domain.model.api


data class Header(
    val isSuccessful: Boolean,
    val resultCode: Int,
    val resultMessage: String
)