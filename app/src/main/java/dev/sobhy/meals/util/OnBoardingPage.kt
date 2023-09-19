package dev.sobhy.meals.util

import dev.sobhy.meals.R

sealed class OnBoardingPage(
    val image: Int,
    val title: String,
    val description: String
){
    object First : OnBoardingPage(
        image = R.raw.animation_meals,
        title = "Welcome to Meals",
        description = "Explore the World of Delicious Meals"
    )

    object Second : OnBoardingPage(
        image = R.raw.animation_lmnqjp3d,
        title = "Discover Meals",
        description = "Browse a variety of meal categories and areas from around the world.\n " +
                "Explore ingredients, instructions, and videos for each meal."
    )

    object Third : OnBoardingPage(
        image = R.raw.animation_search,
        title = "Quick Search",
        description = "Find meals in seconds by searching for their name or keywords.\n" +
                " Our powerful search feature makes it easy."
    )

    object Fourth : OnBoardingPage(
        image = R.raw.animation_enjoy_meal,
        title = "Save Your Favorites",
        description = "Bookmark meals you love for quick access anytime."
    )
}
