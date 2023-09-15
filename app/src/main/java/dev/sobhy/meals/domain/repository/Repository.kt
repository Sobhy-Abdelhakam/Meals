package dev.sobhy.meals.domain.repository

import dev.sobhy.meals.domain.model.area.Area
import dev.sobhy.meals.domain.model.category.Category
import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.domain.model.mealsbything.MealByThing
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getCategoryResponse(): Flow<List<Category>>
    fun getMealDetails(id: Int): Flow<Meal>
    fun getMealsContainString(name: String): Flow<List<Meal>>
    fun getRandomMeal(): Flow<Meal>
    fun getMealsByCategory(category: String): Flow<List<MealByThing>>
    fun getAreasList(): Flow<List<Area>>
    fun getMealsByArea(area: String): Flow<List<MealByThing>>

    suspend fun insertFavoriteMeal(meal: Meal)
    suspend fun deleteMealFromFavorite(meal:Meal)
    fun getFavoriteMeals(): Flow<List<Meal>>
}