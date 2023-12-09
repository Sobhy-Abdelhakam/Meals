package dev.sobhy.meals.data.repository

import android.util.Log
import dev.sobhy.meals.data.local.RoomDao
import dev.sobhy.meals.data.remote.ApiService
import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.domain.repository.Repository
import kotlinx.coroutines.flow.flow

class RepositoryImpl(
    private val apiService: ApiService,
    private val roomDao: RoomDao,
) : Repository {

    override fun getCategoryResponse() = flow {
        val cachedCategory = roomDao.getAllCategories()
        if (cachedCategory.isNotEmpty()) emit(cachedCategory)
        try {
            val networkResponse = apiService.getCategoriesList().categories
            roomDao.insertAllCategories(networkResponse)
            emit(networkResponse)
        } catch (e: Exception) {
            Log.e("repository", e.message.toString())
        }
    }

    override fun getMealDetails(id: Int) = flow {
        val idsOfFavoriteMeals = roomDao.getFavoriteMeals().map {
            it.idMeal
        }
        val meal = if (idsOfFavoriteMeals.contains(id.toString())) {
            roomDao.getFavoriteMealById(id.toString())
        } else {
            apiService.getMealById(id).meals.first()
        }
        emit(meal)
    }

    override fun getMealsContainString(name: String) = flow {
        emit(apiService.getMealsByName(name).meals)
    }

    override fun getRandomMeal() = flow {
        emit(apiService.getSingleRandomMeal().meals.first())
    }

    override fun getMealsByCategory(category: String) = flow {
        emit(apiService.getMealsByCategory(category).meals)
    }

    override fun getAreasList() = flow {
        val cachedAreas = roomDao.getAllAreas()
        if (cachedAreas.isNotEmpty()) emit(cachedAreas)
        try {
            val remoteAreas = apiService.getAreasList().meals
            roomDao.insertAllAreas(remoteAreas)
            emit(remoteAreas)
        } catch (e: Exception) {
            Log.e("repository", e.message.toString())
        }
    }

    override fun getMealsByArea(area: String) = flow {
        emit(apiService.getMealsByArea(area).meals)
    }

    override suspend fun insertFavoriteMeal(meal: Meal) = roomDao.insertFavoriteMeal(meal)
    override suspend fun deleteMealFromFavorite(meal: Meal) = roomDao.deleteMealFromFavorite(meal)
    override fun getFavoriteMeals() = flow {
        emit(roomDao.getFavoriteMeals())
    }
}
