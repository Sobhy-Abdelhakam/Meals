package dev.sobhy.meals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.sobhy.meals.presentation.home.HomeScreen
import dev.sobhy.meals.presentation.home.HomeViewModel
import dev.sobhy.meals.presentation.mealdetails.MealDetailsScreen
import dev.sobhy.meals.presentation.mealdetails.MealDetailsViewModel
import dev.sobhy.meals.presentation.meals.MealsListScreen
import dev.sobhy.meals.presentation.meals.MealsListViewModel
import dev.sobhy.meals.ui.theme.MealsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MealsTheme {
                var appBarState by remember {
                    mutableStateOf(AppBarState())
                }
                Scaffold(
                    topBar = {
                        if(appBarState.show){
                            TopAppBar(
                                title = {
                                    Text(
                                        text = appBarState.title,
                                        fontSize = 24.sp,
                                        fontFamily = FontFamily(Font(R.font.charm_bold)),
                                        modifier = Modifier.padding(2.dp)
                                    )
                                },
                                actions = {
                                    appBarState.actions?.invoke(this)
                                }
                            )
                        }
                    }
                ) {
                    Column(modifier = Modifier.padding(it)) {

                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = "home"
                        ) {
                            composable("home") {
                                val viewModel: HomeViewModel by viewModels()
                                HomeScreen(viewModel, navController) { state ->
                                    appBarState = state
                                }
                            }
                            composable(
                                route = "meals_list/{from}/{thing}",
                                arguments = listOf(
                                    navArgument("from") {
                                        type = NavType.StringType
                                    },
                                    navArgument("thing") {
                                        type = NavType.StringType
                                    }
                                )
                            ) {entry ->
                                val mealsViewModel: MealsListViewModel by viewModels()
                                // // Extract the parameters from the URL
                                val from = entry.arguments?.getString("from")
                                val thing = entry.arguments?.getString("thing")
                                MealsListScreen(
                                    mealsViewModel,
                                    from, thing,
                                    navController,
                                    onComposing = { state -> appBarState = state}
                                )
                            }
                            composable(
                                route = "meal_details/{meal_id}",
                                arguments = listOf(
                                    navArgument("meal_id"){
                                        type = NavType.IntType
                                    }
                                )
                            ){entry ->
                                val mealViewModel: MealDetailsViewModel by viewModels()
                                val mealId = entry.arguments?.getInt("meal_id")
                                MealDetailsScreen(
                                    mealViewModel,
                                    navController,
                                    mealId,
                                    onComposing = { state -> appBarState = state}
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}
