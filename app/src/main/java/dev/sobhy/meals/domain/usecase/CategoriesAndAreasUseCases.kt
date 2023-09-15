package dev.sobhy.meals.domain.usecase

import dev.sobhy.meals.domain.repository.Repository

class CategoriesAndAreasUseCases(
    private val repository: Repository
) {
    fun getCategoryResponse() = repository.getCategoryResponse()
    fun getRandomMeal() = repository.getRandomMeal()
    fun getAreasList() = repository.getAreasList()

}