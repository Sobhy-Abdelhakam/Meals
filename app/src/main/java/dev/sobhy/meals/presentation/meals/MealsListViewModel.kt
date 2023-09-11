package dev.sobhy.meals.presentation.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.meals.domain.model.mealsbything.MealByThing
import dev.sobhy.meals.domain.usecase.UseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealsListViewModel @Inject constructor(private val useCases: UseCases): ViewModel() {
    private val _mealsState = MutableStateFlow(MealsState())
    val mealsState = _mealsState.asStateFlow()

    fun getMealsByCategory(category: String){
        viewModelScope.launch {
            _mealsState.update {
                it.copy(isLoading = true)
            }
            val response = useCases.getMealsByCategory(category)
            _mealsState.update {
                it.copy(isLoading = false, mealsList = response.meals)
            }
        }
    }

    fun getMealsByArea(area: String){
        viewModelScope.launch {
            _mealsState.update {
                it.copy(isLoading = true)
            }
            val response = useCases.getMealsByArea(area)
            _mealsState.update {
                it.copy(isLoading = false, mealsList = response.meals)
            }
        }
    }

}
data class MealsState(
    val isLoading: Boolean = false,
    val mealsList: List<MealByThing> = emptyList()
)