package dev.sobhy.meals.data.repository

import android.util.Log
import dev.sobhy.meals.data.local.RoomDao
import dev.sobhy.meals.data.remote.ApiService
import dev.sobhy.meals.domain.model.category.Category
import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RepositoryImpl(
    private val apiService: ApiService,
    private val roomDao: RoomDao
) : Repository {

    override suspend fun getCategoryResponse(): Flow<List<Category>> {
        return flow {
            val cachedCategory = roomDao.getAllCategories()
            if (cachedCategory.isNotEmpty()) emit(cachedCategory)
            try {
                val networkResponse = apiService.getCategoriesList().categories
                roomDao.insertAllCategories(networkResponse)
                emit(networkResponse)
            } catch (e: Exception) {
                Log.e("repository", e.message.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getMealDetails(id: Int) = apiService.getMealById(id)

    override suspend fun getMealsContainString(name: String) = apiService.getMealsByName(name)

    override suspend fun getRandomMeal() = flow {
        val randomCachedMeal = roomDao.getRandomMeal()
        emit(randomCachedMeal)
        try {
            val randomRemoteMeal = apiService.getSingleRandomMeal().meals.first()
            roomDao.insertRandomMeal(randomRemoteMeal)
//            emit(randomRemoteMeal)
        } catch (e: Exception){
            Log.e("repository", e.message.toString())
        }
    }.flowOn(Dispatchers.IO)
//        apiService.getSingleRandomMeal().meals.first()

    override suspend fun getMealsByCategory(category: String) =
        apiService.getMealsByCategory(category)

    override suspend fun getIngredientsList() = apiService.getIngredientsList()

    override suspend fun getMealsByIngredients(ingredient: String) =
        apiService.getMealsByIngredient(ingredient)

    override suspend fun getAreasList() = flow {
        val cachedAreas = roomDao.getAllAreas()
        if (cachedAreas.isNotEmpty()) emit(cachedAreas)
        try {
            val remoteAreas = apiService.getAreasList().meals
            roomDao.insertAllAreas(remoteAreas)
            emit(remoteAreas)
        } catch (e: Exception){
            Log.e("repository", e.message.toString())
        }
    }.flowOn(Dispatchers.IO)
//        apiService.getAreasList()

    override suspend fun getMealsByArea(area: String) = apiService.getMealsByArea(area)

}