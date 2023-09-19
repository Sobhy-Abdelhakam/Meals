package dev.sobhy.meals.presentation.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.meals.domain.usecase.SaveOnBoardUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val saveOnBoardUseCase: SaveOnBoardUseCase
): ViewModel() {

    fun saveOnBoardState(){
        viewModelScope.launch(Dispatchers.IO) {
            saveOnBoardUseCase()
        }
    }
}