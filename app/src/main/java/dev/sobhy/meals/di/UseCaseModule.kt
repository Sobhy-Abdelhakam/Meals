package dev.sobhy.meals.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sobhy.meals.domain.repository.Repository
import dev.sobhy.meals.domain.usecase.FavAndSearchUseCase
import dev.sobhy.meals.domain.usecase.ListOfMealsUseCase
import dev.sobhy.meals.domain.usecase.MealDetailsUseCase
import dev.sobhy.meals.domain.usecase.CategoriesAndAreasUseCases

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideCategoriesAndAreasUseCase(
        repository: Repository
    ): CategoriesAndAreasUseCases {
        return CategoriesAndAreasUseCases(repository)
    }
    @Provides
    fun provideFavAndSearchUseCase(
        repository: Repository
    ):FavAndSearchUseCase{
        return FavAndSearchUseCase(repository)
    }
    @Provides
    fun provideListOfMealsUseCase(
        repository: Repository
    ):ListOfMealsUseCase{
        return ListOfMealsUseCase(repository)
    }
    @Provides
    fun provideMealDetailsUseCase(
        repository: Repository
    ):MealDetailsUseCase{
        return MealDetailsUseCase(repository)
    }
}