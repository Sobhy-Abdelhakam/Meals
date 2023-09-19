package dev.sobhy.meals.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dev.sobhy.meals.domain.repository.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManagerImpl(
    private val context: Context
): DataStoreManager {
    override suspend fun saveOnBoardingState() {
        context.dataStore.edit {preferences ->
            preferences[PreferencesKey.onBoardingKey] = true
        }
    }

    override fun readOnBoardingState(): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[PreferencesKey.onBoardingKey] ?: false
            }
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "on_boarding_pref")

private object PreferencesKey {
    val onBoardingKey = booleanPreferencesKey(name = "on_boarding_completed")
}

