package dev.sobhy.meals.presentation.meals

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.domain.model.mealsbything.MealByThing
import dev.sobhy.meals.domain.usecase.UseCases
import kotlinx.coroutines.Dispatchers
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
        _mealsState.update {
            it.copy(mealsList = emptyList(),mealsListLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _mealsState.update {
                    it.copy(
                        mealsListLoading = false,
                        mealsList = useCases
                            .getMealsByCategory(category)
                            .meals
                    )
                }
            } catch (e: Exception){
                Log.e("meals list view model", e.message.toString())
            }
        }
    }

    fun getMealsByArea(area: String){
        _mealsState.update {
            it.copy(mealsList = emptyList(), mealsListLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _mealsState.update {
                    it.copy(
                        mealsListLoading = false,
                        mealsList = useCases
                            .getMealsByArea(area)
                            .meals
                    )
                }
            } catch (e: Exception){
                Log.e("meals list view model", e.message.toString())
            }
        }
    }

}
data class MealsState(
    val mealsListLoading: Boolean = false,
    val mealsList: List<MealByThing> = emptyList(),
)