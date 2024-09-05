package com.example.domain.api.usecase

import com.example.domain.api.ResponseState
import com.example.domain.api.repository.MapApiRepository

class GetReverseGeoCodingUseCase (private val mapApiRepository: MapApiRepository) {
    suspend operator fun invoke(posX: Double, posY: Double): ResponseState {
        return mapApiRepository.getReverseGeoCoding(posX, posY)
    }
}