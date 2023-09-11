package dev.sobhy.meals.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.sobhy.meals.domain.model.area.Area
import dev.sobhy.meals.domain.model.category.Category
import dev.sobhy.meals.domain.model.meal.Meal

@Database(entities = [Category::class, Area::class, Meal::class], version = 1)
abstract class MealsDatabase: RoomDatabase() {
    abstract fun mealsDao(): RoomDao
}