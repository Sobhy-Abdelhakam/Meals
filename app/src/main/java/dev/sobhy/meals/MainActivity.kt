package dev.sobhy.meals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.sobhy.meals.navigation.Screens
import dev.sobhy.meals.presentation.favoriteandsearch.FavAndSearchScreen
import dev.sobhy.meals.presentation.favoriteandsearch.FavAndSearchViewModel
import dev.sobhy.meals.presentation.home.HomeScreen
import dev.sobhy.meals.presentation.home.HomeViewModel
import dev.sobhy.meals.presentation.mealdetails.MealDetailsScreen
import dev.sobhy.meals.presentation.mealdetails.MealDetailsViewModel
import dev.sobhy.meals.presentation.meals.MealsListScreen
import dev.sobhy.meals.presentation.meals.MealsListViewModel
import dev.sobhy.meals.presentation.onboard.WelcomeScreen
import dev.sobhy.meals.presentation.onboard.WelcomeViewModel
import dev.sobhy.meals.ui.theme.MealsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.splashCondition
            }
        }
        setContent {
            MealsTheme {
                SetupNavGraph(startDestination = viewModel.startDestination)
            }
        }
    }
}

@Composable
fun SetupNavGraph(
    startDestination: String,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Screens.Welcome.route) {
            val welcomeViewModel: WelcomeViewModel = hiltViewModel()
            WelcomeScreen(
                skipClick = {
                    welcomeViewModel.saveOnBoardState()
                    navController.popBackStack()
                    navController.navigate(Screens.Home.route)
                },
                getStartButton = {
                    welcomeViewModel.saveOnBoardState()
                    navController.popBackStack()
                    navController.navigate(Screens.Home.route)
                },
            )
        }
        composable(Screens.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val state by homeViewModel.homeState.collectAsState()
            HomeScreen(
                state = state,
                getRandomMeal = { homeViewModel.getRandomMeal() },
                searchIconClick = {
                    navController.navigate(
                        "${Screens.FavAndSearchScreen.route}/search",
                    )
                },
                favoriteIconClick = {
                    navController.navigate(
                        "${Screens.FavAndSearchScreen.route}/favorites",
                    )
                },
                randomMealClick = {
                    navController.navigate(
                        "${Screens.MealDetails.route}/$it",
                    )
                },
                areasMealClick = {
                    navController.navigate(
                        "${Screens.MealsList.route}/area/$it",
                    )
                },
                categoriesMealsClick = {
                    navController.navigate(
                        "${Screens.MealsList.route}/category/$it",
                    )
                },
            )
        }
        composable(
            route = "${Screens.MealsList.route}/{from}/{thing}",
            arguments = listOf(
                navArgument("from") { type = NavType.StringType },
                navArgument("thing") { type = NavType.StringType },
            ),
        ) { entry ->
            val mealsListViewModel: MealsListViewModel = hiltViewModel()
            val mealsState by mealsListViewModel.mealsState.collectAsState()
            val thing = entry.arguments?.getString("thing")
            MealsListScreen(
                thing = thing,
                errorRefreshButton = { mealsListViewModel.getData() },
                mealItemClick = {
                    navController.navigate(
                        "${Screens.MealDetails.route}/$it",
                    )
                },
                mealsState = mealsState,
            )
        }
        composable(
            route = "${Screens.FavAndSearchScreen.route}/{from}",
            arguments = listOf(
                navArgument("from") { type = NavType.StringType },
            ),
        ) { entry ->
            val favAndSearchViewModel: FavAndSearchViewModel = hiltViewModel()
            val favAndSearchState by favAndSearchViewModel.favState.collectAsState()
            val from = entry.arguments?.getString("from")

            FavAndSearchScreen(
                favAndSearchState = favAndSearchState,
                favoriteMeals = { favAndSearchViewModel.getFavoriteMeals() },
                emptyingList = {
                    favAndSearchViewModel.makeListEmpty()
                },
                searchMeals = {
                    favAndSearchViewModel.getMealsContainString(it)
                },
                from = from,
                mealItemClick = {
                    navController.navigate(
                        "${Screens.MealDetails.route}/$it",
                    )
                },
            )
        }

        composable(
            route = "${Screens.MealDetails.route}/{meal_id}",
            arguments = listOf(
                navArgument("meal_id") {
                    type = NavType.IntType
                },
            ),
        ) {
            val mealViewModel: MealDetailsViewModel = hiltViewModel()
            val state by mealViewModel.mealState.collectAsState()
            MealDetailsScreen(
                state = state,
                onFavoriteIconClick = {
                    mealViewModel.toggleFavoriteState(it)
                },
                onRefreshButtonClick = {
                    mealViewModel.getData()
                },
                onBackButtonClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}
