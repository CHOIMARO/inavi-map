package com.example.domain.map.usecase

import com.example.domain.map.repository.MapRepository

class SetSdkAppKeyUseCase(private val mapRepository: MapRepository) {
    suspend operator fun invoke() {
        mapRepository.setSdkAppKey()
    }
}