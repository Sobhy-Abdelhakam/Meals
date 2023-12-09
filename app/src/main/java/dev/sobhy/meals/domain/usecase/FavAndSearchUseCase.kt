package dev.sobhy.meals.domain.usecase

import dev.sobhy.meals.domain.repository.Repository

class FavAndSearchUseCase(
    private val repository: Repository,
) {
    fun getMealsContainString(name: String) =
        repository.getMealsContainString(name)
    fun getFavoriteMeals() = repository.getFavoriteMeals()
}
