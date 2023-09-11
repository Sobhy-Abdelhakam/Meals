package dev.sobhy.meals.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sobhy.meals.domain.repository.Repository
import dev.sobhy.meals.domain.usecase.UseCases

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideUseCase(repository: Repository): UseCases {
        return UseCases(repository)
    }
}