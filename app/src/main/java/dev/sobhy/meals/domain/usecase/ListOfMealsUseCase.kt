package dev.sobhy.meals.domain.usecase

import dev.sobhy.meals.domain.repository.Repository

class ListOfMealsUseCase(
    private val repository: Repository
) {
    fun getMealsByCategory(category: String) = repository.getMealsByCategory(category)
    fun getMealsByArea(area: String) = repository.getMealsByArea(area)
}