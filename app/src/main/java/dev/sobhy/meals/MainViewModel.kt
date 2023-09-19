package dev.sobhy.meals

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.meals.domain.usecase.ReadOnBoardUseCase
import dev.sobhy.meals.navigation.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    readOnBoardUseCase: ReadOnBoardUseCase
): ViewModel() {
    var splashCondition by mutableStateOf(true)
        private set
    var startDestination by mutableStateOf(Screens.Welcome.route)
        private set

    init {
        readOnBoardUseCase().onEach {startFromHome ->
            startDestination = if(startFromHome){
                Screens.Home.route
            } else {
                Screens.Welcome.route
            }
            delay(300)
            splashCondition = false
        }.launchIn(viewModelScope)
    }
}