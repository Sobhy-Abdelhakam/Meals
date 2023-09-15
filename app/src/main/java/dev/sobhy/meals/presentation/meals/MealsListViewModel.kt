package dev.sobhy.meals.presentation.meals

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.meals.domain.model.mealsbything.MealByThing
import dev.sobhy.meals.domain.usecase.UseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealsListViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {
    private val _mealsState = MutableStateFlow(MealsState())
    val mealsState = _mealsState.asStateFlow()

    fun getMealsByCategory(category: String) {
        _mealsState.value = MealsState(
            mealsList = emptyList(),
            mealsListLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _mealsState.update {
                    it.copy(
                        mealsList = useCases
                            .getMealsByCategory(category)
                            .meals,
                        mealsListLoading = false,
                    )
                }
            } catch (e: Exception) {
                Log.e("meals list view model", e.message.toString())
            }
        }
    }

    fun getMealsByArea(area: String) {
        _mealsState.value = MealsState(
            mealsList = emptyList(),
            mealsListLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _mealsState.update {
                    it.copy(
                        mealsList = useCases
                            .getMealsByArea(area)
                            .meals,
                        mealsListLoading = false,
                    )
                }
            } catch (e: Exception) {
                Log.e("meals list view model", e.message.toString())
            }
        }
    }

}

data class MealsState(
    val mealsListLoading: Boolean = true,
    val mealsList: List<MealByThing> = emptyList(),
)