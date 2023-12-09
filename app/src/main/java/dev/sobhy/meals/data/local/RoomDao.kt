package dev.sobhy.meals.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import dev.sobhy.meals.domain.model.area.Area
import dev.sobhy.meals.domain.model.category.Category
import dev.sobhy.meals.domain.model.meal.Meal

@Dao
interface RoomDao {
    @Upsert(entity = Category::class)
    suspend fun insertAllCategories(categories: List<Category>)

    @Query("select * from category")
    suspend fun getAllCategories(): List<Category>

    @Upsert(entity = Area::class)
    suspend fun insertAllAreas(categories: List<Area>)

    @Query("select * from areas")
    suspend fun getAllAreas(): List<Area>

    @Insert(entity = Meal::class)
    suspend fun insertFavoriteMeal(meal: Meal)

    @Delete(entity = Meal::class)
    suspend fun deleteMealFromFavorite(meal: Meal)

    @Query("select * from meal where idMeal = :id")
    suspend fun getFavoriteMealById(id: String): Meal

    @Query("SELECT * FROM meal")
    suspend fun getFavoriteMeals(): List<Meal>
}
