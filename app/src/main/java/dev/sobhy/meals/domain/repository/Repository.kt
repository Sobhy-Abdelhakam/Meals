package dev.sobhy.meals.domain.repository

import dev.sobhy.meals.domain.model.area.Area
import dev.sobhy.meals.domain.model.category.Category
import dev.sobhy.meals.domain.model.ingredients.IngredientsList
import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.domain.model.meal.MealDetails
import dev.sobhy.meals.domain.model.mealsbything.MealsByThingList
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getCategoryResponse(): Flow<List<Category>>
    suspend fun getMealDetails(id: Int): MealDetails
    suspend fun getMealsContainString(name: String): MealDetails
    suspend fun getRandomMeal(): Flow<Meal>
    suspend fun getMealsByCategory(category: String): MealsByThingList
    suspend fun getIngredientsList(): IngredientsList
    suspend fun getMealsByIngredients(ingredient: String): MealsByThingList
    suspend fun getAreasList(): Flow<List<Area>>
    suspend fun getMealsByArea(area: String): MealsByThingList
}