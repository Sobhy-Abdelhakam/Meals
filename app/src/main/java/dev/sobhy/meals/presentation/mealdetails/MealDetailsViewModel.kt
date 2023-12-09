package dev.sobhy.meals.presentation.mealdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.domain.usecase.MealDetailsUseCase
import dev.sobhy.meals.presentation.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealDetailsViewModel @Inject constructor(
    private val useCases: MealDetailsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _mealState = MutableStateFlow<UiState<Meal>>(UiState.Loading)
    val mealState = _mealState.asStateFlow()

    private val mealId = savedStateHandle.get<Int>("meal_id") ?: 0

    init {
        getData()
    }

    fun getData() = getMeal(mealId)

    private fun getMeal(id: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            _mealState.value = UiState.Loading
            useCases.getMealDetails(id)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _mealState.value = UiState.Error(e.toString())
                }
                .collect { meal ->
                    _mealState.value = UiState.Success(meal)
                }
        }
    }

    fun toggleFavoriteState(meal: Meal) {
        viewModelScope.launch {
            val newIsFavorite = !meal.isFavorite
            updateFavoriteState(meal, newIsFavorite)
        }
    }

    private suspend fun updateFavoriteState(meal: Meal, isFavorite: Boolean) {
        val updatedMeal = meal.copy(isFavorite = isFavorite)

        if (isFavorite) {
            useCases.insertFavoriteMeal(updatedMeal)
        } else {
            useCases.deleteMealFromFavorite(updatedMeal)
        }

        _mealState.value = UiState.Success(updatedMeal)
    }
}
