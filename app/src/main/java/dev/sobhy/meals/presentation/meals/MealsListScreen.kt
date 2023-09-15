package dev.sobhy.meals.presentation.meals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import dev.sobhy.meals.domain.model.mealsbything.MealByThing
import dev.sobhy.meals.util.AnimatedShimmer
import dev.sobhy.meals.util.AppBarState

@Composable
fun MealsListScreen(
    mealsViewModel: MealsListViewModel,
    from: String?,
    thing: String?,
    navController: NavHostController,
    onComposing: (AppBarState) -> Unit
) {
    when (from) {
        "category" -> mealsViewModel.getMealsByCategory(thing!!)
        "area" -> mealsViewModel.getMealsByArea(thing!!)
    }

    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                show = true,
                title = thing ?: "",
                actions = { }
            ),
        )
    }

    val mealsState by mealsViewModel.mealsState.collectAsState()

    AnimatedShimmer(showShimmer = mealsState.mealsListLoading)
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(mealsState.mealsList) { meal ->
            MealsItem(
                meal,
                onItemClick = {
                    navController.navigate("meal_details/${meal.idMeal.toInt()}")
                }
            )
        }
    }

}

@Composable
fun MealsItem(mealByThing: MealByThing, onItemClick: () -> Unit) {
    Card(modifier = Modifier
        .padding(8.dp)
        .clickable { onItemClick() }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = mealByThing.strMealThumb,
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Text(
                text = mealByThing.strMeal,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally),
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
