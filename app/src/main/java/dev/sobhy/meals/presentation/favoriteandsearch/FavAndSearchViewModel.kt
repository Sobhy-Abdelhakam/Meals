package dev.sobhy.meals.presentation.favoriteandsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.meals.presentation.UiState
import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.domain.usecase.FavAndSearchUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavAndSearchViewModel @Inject constructor(
    private val useCases: FavAndSearchUseCase
) : ViewModel() {
    private val _favState = MutableStateFlow<UiState<List<Meal>?>>(UiState.Success(emptyList()))
    val favState = _favState.asStateFlow()

    fun getFavoriteMeals(){
        viewModelScope.launch(Dispatchers.Main) {
            _favState.value = UiState.Loading
            useCases.getFavoriteMeals()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _favState.value = UiState.Error(e.toString())
                }
                .collect{listOfMeal ->
                    _favState.value = UiState.Success(listOfMeal)
                }
        }
    }

    fun getMealsContainString(text: String){
        viewModelScope.launch(Dispatchers.Main) {
            _favState.value = UiState.Loading
            useCases.getMealsContainString(text)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _favState.value = UiState.Error(e.toString())
                }
                .collect{listOfMeal ->
                    _favState.value = UiState.Success(listOfMeal)
                }
        }
    }

    fun makeListEmpty(){
        _favState.value = UiState.Success(emptyList())
    }
}