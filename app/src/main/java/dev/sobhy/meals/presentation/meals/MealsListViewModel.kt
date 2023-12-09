package dev.sobhy.meals.presentation.meals

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.meals.domain.model.mealsbything.MealByThing
import dev.sobhy.meals.domain.usecase.ListOfMealsUseCase
import dev.sobhy.meals.presentation.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealsListViewModel @Inject constructor(
    private val useCases: ListOfMealsUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _mealsState = MutableStateFlow<UiState<List<MealByThing>>>(UiState.Loading)
    val mealsState = _mealsState.asStateFlow()

    private var from: String = savedStateHandle.get<String>("from") ?: "category"
    private var thing: String = savedStateHandle.get<String>("thing") ?: "Chicken"

    init {
        getData()
    }

    fun getData() {
        when (from) {
            "category" -> getMealsByCategory(thing)
            "area" -> getMealsByArea(thing)
        }
    }

    private fun getMealsByCategory(category: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _mealsState.value = UiState.Loading
            useCases.getMealsByCategory(category)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _mealsState.value = UiState.Error(e.toString())
                }
                .collect { listOfMeals ->
                    _mealsState.value = UiState.Success(listOfMeals)
                }
        }
    }

    private fun getMealsByArea(area: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _mealsState.value = UiState.Loading
            useCases.getMealsByArea(area)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _mealsState.value = UiState.Error(e.toString())
                }
                .collect { listOfMeals ->
                    _mealsState.value = UiState.Success(listOfMeals)
                }
        }
    }
}
