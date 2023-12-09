package dev.sobhy.meals.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.sobhy.meals.data.repository.DataStoreManagerImpl
import dev.sobhy.meals.domain.repository.DataStoreManager
import dev.sobhy.meals.domain.usecase.ReadOnBoardUseCase
import dev.sobhy.meals.domain.usecase.SaveOnBoardUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppEntryModule {
    @Provides
    @Singleton
    fun provideDataStoreManager(
        @ApplicationContext
        appContext: Context,
    ): DataStoreManager = DataStoreManagerImpl(appContext)

    @Provides
    @Singleton
    fun provideReadOnBoardUseCase(
        dataStoreManager: DataStoreManager,
    ) = ReadOnBoardUseCase(dataStoreManager)

    @Provides
    @Singleton
    fun provideSaveOnBoardUseCase(
        dataStoreManager: DataStoreManager,
    ) = SaveOnBoardUseCase(dataStoreManager)
}
