package dev.sobhy.meals.presentation.favoriteandsearch

import android.util.Log
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
class FavAndSearchViewModel @Inject constructor(private val useCases: UseCases) : ViewModel() {
    private val _favState = MutableStateFlow(FavAndSearchState())
    val favState = _favState.asStateFlow()

    fun getFavoriteMeals(){
        viewModelScope.launch {
            _favState.update {
                it.copy(responseMealsList = useCases.getFavoriteMeals())
            }

//            _favState.update {
//                it.copy(searchFavLoading = true, responseMealsList = emptyList())
//            }
//            val response = useCases.getFavoriteMeals()
//            _favState.update {
//                it.copy(searchFavLoading = false, responseMealsList = response)
//            }
        }
    }

    fun getMealsContainString(text: String){
        viewModelScope.launch {
            try {
                _favState.update {
                    it.copy(searchFavLoading = true, responseMealsList = emptyList())
                }
                val response = useCases.getMealsContainString(text)
                _favState.update {
                    it.copy(searchFavLoading = false, responseMealsList = response)
                }
            } catch (e: Exception){
                Log.e("Fav viewModel", e.message.toString())
            }
        }
    }

    fun makeListEmpty(){
        _favState.value = FavAndSearchState(
            searchFavLoading = false,
            responseMealsList = emptyList()
        )
    }

}

data class FavAndSearchState(
    val searchFavLoading: Boolean = false,
    val responseMealsList: List<Meal> = emptyList()
)