package dev.sobhy.meals.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    suspend fun saveOnBoardingState()
    fun readOnBoardingState(): Flow<Boolean>
}