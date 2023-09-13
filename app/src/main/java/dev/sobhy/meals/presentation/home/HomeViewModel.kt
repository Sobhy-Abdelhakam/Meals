package dev.sobhy.meals.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.meals.domain.model.area.Area
import dev.sobhy.meals.domain.model.category.Category
import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.domain.usecase.UseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCases: UseCases) : ViewModel() {
    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        getRandomMeal()
        getCategories()
        getAreas()
    }

    fun getRandomMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val randomResponse = useCases.getRandomMeal()
                _homeState.update {
                    it.copy(mealDetails = randomResponse)
                }
            } catch (e: Exception) {
                Log.e("viewModel", e.message.toString())
            }
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            _homeState.update {
                it.copy(isLoading = true)
            }
            val categoryResponse = useCases.getCategoryResponse()
            categoryResponse.collect { categories ->
                _homeState.update {
                    it.copy(isLoading = false, categoryResponse = categories)
                }
            }
        }
    }

    private fun getAreas() {
        viewModelScope.launch {
            _homeState.update {
                it.copy(isLoading = true)
            }
            val areasResponse = useCases.getAreasList()
            areasResponse.collect { areas ->
                _homeState.update {
                    it.copy(isLoading = false, areasResponse = areas)
                }
            }
        }
    }
}

data class HomeState(
    val isLoading: Boolean = false,
    val mealDetails: Meal? = null,
    val categoryResponse: List<Category> = emptyList(),
    val areasResponse: List<Area> = emptyList()
)