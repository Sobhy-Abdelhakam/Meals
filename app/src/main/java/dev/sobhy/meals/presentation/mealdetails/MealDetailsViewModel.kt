package dev.sobhy.meals.presentation.mealdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.domain.usecase.UseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealDetailsViewModel @Inject constructor(private val useCases: UseCases) : ViewModel() {
    private val _mealState = MutableStateFlow(MealDetailsState())
    val mealState = _mealState.asStateFlow()

    fun getMeal(id: Int) {
        _mealState.value = MealDetailsState(meal = null, mealDetailsLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                useCases.getMealDetails(id).collect { meal ->
                    _mealState.update {
                        it.copy(mealDetailsLoading = false, meal = meal)
                    }
                }
            } catch (e: Exception){
                Log.e("mealDetails view model", e.message.toString())
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

data class MealDetailsState(
    val mealDetailsLoading: Boolean = false,
    val meal: Meal? = null
)