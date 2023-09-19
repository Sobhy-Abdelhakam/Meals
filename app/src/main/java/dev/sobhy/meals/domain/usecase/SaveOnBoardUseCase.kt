package dev.sobhy.meals.domain.usecase

import dev.sobhy.meals.domain.repository.DataStoreManager

class SaveOnBoardUseCase(
    private val dataStoreManager: DataStoreManager
) {
    suspend operator fun invoke() = dataStoreManager.saveOnBoardingState()
}