package dev.sobhy.meals.presentation.mealdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.domain.usecase.UseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealDetailsViewModel @Inject constructor(private val useCases: UseCases):ViewModel() {
    private val _mealState = MutableStateFlow(MealDetailsState())
    val mealState = _mealState.asStateFlow()

    fun getMeal(id: Int){
        viewModelScope.launch {
            _mealState.update {
                it.copy(isLoading = true)
            }
            val meal = useCases.getMealDetails(id).meals.first()
            _mealState.update {
                it.copy(isLoading = false, meal = meal)
            }
        }
    }
}

data class MealDetailsState(
    val isLoading: Boolean = false,
    val meal: Meal? = null
)