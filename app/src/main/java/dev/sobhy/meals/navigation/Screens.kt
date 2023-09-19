package dev.sobhy.meals.navigation

sealed class Screens(val route: String) {
    object Welcome : Screens(route = "welcome_screen")
    object Home : Screens(route = "home_screen")
    object MealsList : Screens(route = "meals_list_screen")
    object MealDetails : Screens(route = "meal_details_screen")
    object FavAndSearchScreen : Screens(route = "fav_search_screen")
}
