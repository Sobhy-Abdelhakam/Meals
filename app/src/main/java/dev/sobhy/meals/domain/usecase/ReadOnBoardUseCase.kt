package dev.sobhy.meals.domain.usecase

import dev.sobhy.meals.domain.repository.DataStoreManager

class ReadOnBoardUseCase(
    private val dataStoreManager: DataStoreManager,
) {
    operator fun invoke() = dataStoreManager.readOnBoardingState()
}
