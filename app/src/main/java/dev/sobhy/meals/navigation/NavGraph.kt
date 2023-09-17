package dev.sobhy.meals.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.sobhy.meals.presentation.favoriteandsearch.FavAndSearchScreen
import dev.sobhy.meals.presentation.home.HomeScreen
import dev.sobhy.meals.presentation.mealdetails.MealDetailsScreen
import dev.sobhy.meals.presentation.meals.MealsListScreen
import dev.sobhy.meals.util.AppBarState

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String,
    changeAppBarState: (AppBarState) -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screens.Home.route) {
            HomeScreen(
                navController = navController,
                onComposing = changeAppBarState
            )
        }
        composable(
            route = "${Screens.MealsList.route}/{from}/{thing}",
            arguments = listOf(
                navArgument("from") {
                    type = NavType.StringType
                },
                navArgument("thing") {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            val from = entry.arguments?.getString("from")
            val thing = entry.arguments?.getString("thing")
            MealsListScreen(
                from = from,
                thing = thing,
                navController = navController,
                onComposing = changeAppBarState
            )
        }
        composable(
            route = "${Screens.FavAndSearchScreen.route}/{from}",
            arguments = listOf(
                navArgument("from") {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            val from = entry.arguments?.getString("from")
            FavAndSearchScreen(
                from = from,
                navController = navController,
                onComposing = changeAppBarState
            )
        }

        composable(
            route = "${Screens.MealDetails.route}/{meal_id}",
            arguments = listOf(
                navArgument("meal_id") {
                    type = NavType.IntType
                }
            )
        ) { entry ->
            val mealId = entry.arguments?.getInt("meal_id")
            MealDetailsScreen(
                navController = navController,
                mealId = mealId,
                onComposing = changeAppBarState
            )
        }
    }

}