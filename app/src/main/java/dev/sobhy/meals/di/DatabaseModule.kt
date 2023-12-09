package dev.sobhy.meals.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.sobhy.meals.data.local.MealsDatabase
import dev.sobhy.meals.data.local.RoomDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext
        appContext: Context,
    ): MealsDatabase {
        return Room.databaseBuilder(
            appContext,
            MealsDatabase::class.java,
            "meals_database",
        ).build()
    }

    @Provides
    @Singleton
    fun provideCategoriesDao(mealsDatabase: MealsDatabase): RoomDao {
        return mealsDatabase.mealsDao()
    }
}
