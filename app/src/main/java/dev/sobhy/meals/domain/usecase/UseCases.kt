package dev.sobhy.meals.domain.usecase

import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.domain.repository.Repository

class UseCases(
    private val repository: Repository
) {
    suspend fun getCategoryResponse() = repository.getCategoryResponse()

    suspend fun getMealDetails(id: Int) = repository.getMealDetails(id)

    suspend fun getMealsContainString(name: String) =
        repository.getMealsContainString(name)

    suspend fun getRandomMeal() = repository.getRandomMeal()

    suspend fun getMealsByCategory(category: String) = repository.getMealsByCategory(category)

    suspend fun getIngredientsList() = repository.getIngredientsList()

    suspend fun getMealsByIngredients(ingredient: String) =
        repository.getMealsByIngredients(ingredient)

    suspend fun getAreasList() = repository.getAreasList()

    suspend fun getMealsByArea(area: String) = repository.getMealsByArea(area)

    suspend fun insertFavoriteMeal(meal: Meal) = repository.insertFavoriteMeal(meal)
    suspend fun deleteMealFromFavorite(meal: Meal) = repository.deleteMealFromFavorite(meal)
    suspend fun getFavoriteMeals() = repository.getFavoriteMeals()
}