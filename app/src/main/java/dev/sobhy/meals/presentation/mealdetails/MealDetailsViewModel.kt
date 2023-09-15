package dev.sobhy.meals.presentation.mealdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.meals.presentation.UiState
import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.domain.usecase.MealDetailsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealDetailsViewModel @Inject constructor(
    private val useCases: MealDetailsUseCase
) : ViewModel() {
    private val _mealState = MutableStateFlow<UiState<Meal>>(UiState.Loading)
    val mealState = _mealState.asStateFlow()

    fun getMeal(id: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            _mealState.value = UiState.Loading
            useCases.getMealDetails(id)
//            useCases.getMealDetails(id)
                .flowOn(Dispatchers.IO)
                .catch {e ->
                    _mealState.value = UiState.Error(e.toString())
                }
                .collect{meal ->
                    _mealState.value = UiState.Success(meal)
                }
        }

    }

    fun insertFavoriteMeal(meal: Meal) = viewModelScope.launch(Dispatchers.IO) {
        useCases.insertFavoriteMeal(meal.also {
            it.isFavorite = true
        })
    }

    fun deleteMealFromFavorite(meal: Meal) = viewModelScope.launch(Dispatchers.IO) {
        useCases.deleteMealFromFavorite(meal.also {
            it.isFavorite = false
        })
    }
}

//data class MealDetailsState(
//    val mealDetailsLoading: Boolean = false,
//    val meal: Meal? = null
//)