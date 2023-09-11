package dev.sobhy.meals.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sobhy.meals.data.local.RoomDao
import dev.sobhy.meals.data.remote.ApiService
import dev.sobhy.meals.data.repository.RepositoryImpl
import dev.sobhy.meals.domain.repository.Repository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideRepository(
        apiService: ApiService,
        roomDao: RoomDao
    ):Repository{
        return RepositoryImpl(apiService, roomDao)
    }
}