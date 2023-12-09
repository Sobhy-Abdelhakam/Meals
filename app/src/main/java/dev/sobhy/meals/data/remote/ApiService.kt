package dev.sobhy.meals.data.remote

import dev.sobhy.meals.domain.model.area.AreaList
import dev.sobhy.meals.domain.model.category.CategoryResponse
import dev.sobhy.meals.domain.model.meal.MealDetails
import dev.sobhy.meals.domain.model.mealsbything.MealsByThingList
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("categories.php")
    suspend fun getCategoriesList(): CategoryResponse

    @GET("search.php")
    suspend fun getMealsByName(
        @Query("s")
        name: String,
    ): MealDetails

    @GET("lookup.php")
    suspend fun getMealById(
        @Query("i")
        id: Int,
    ): MealDetails

    @GET("random.php")
    suspend fun getSingleRandomMeal(): MealDetails

    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query("c")
        category: String,
    ): MealsByThingList

    @GET("filter.php")
    suspend fun getMealsByArea(
        @Query("a")
        area: String,
    ): MealsByThingList

    @GET("list.php?a=list")
    suspend fun getAreasList(): AreaList
}
