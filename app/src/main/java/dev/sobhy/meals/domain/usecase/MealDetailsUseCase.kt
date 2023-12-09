package dev.sobhy.meals.domain.usecase

import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.domain.repository.Repository

class MealDetailsUseCase(
    private val repository: Repository,
) {
    fun getMealDetails(id: Int) = repository.getMealDetails(id)
    suspend fun insertFavoriteMeal(meal: Meal) = repository.insertFavoriteMeal(meal)
    suspend fun deleteMealFromFavorite(meal: Meal) = repository.deleteMealFromFavorite(meal)
}
