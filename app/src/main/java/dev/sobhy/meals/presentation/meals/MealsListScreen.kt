package dev.sobhy.meals.presentation.meals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import dev.sobhy.meals.domain.model.mealsbything.MealByThing
import dev.sobhy.mealzapp.ui.composable.Loader

@Composable
fun MealsListScreen(
    mealsViewModel: MealsListViewModel,
    from: String?,
    thing: String?,
    navController: NavHostController
) {
    when(from){
        "category" -> mealsViewModel.getMealsByCategory(thing!!)
        "area" -> mealsViewModel.getMealsByArea(thing!!)
    }

    val mealsState by mealsViewModel.mealsState.collectAsState()
    Box {
        Loader(shouldShow = mealsState.isLoading)
    }
    LazyColumn(){
        items(mealsState.mealsList){
            MealsItem(it, onItemClick = {navController.navigate("meal_details/${it.idMeal.toInt()}")})
        }
    }
}

@Composable
fun MealsItem(mealByThing: MealByThing, onItemClick: ()-> Unit) {
    Card(modifier = Modifier.padding(8.dp).clickable { onItemClick() }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = mealByThing.strMealThumb,
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f)
            )
            Text(text = mealByThing.strMeal, fontSize = 25.sp,  modifier = Modifier.padding(8.dp))
        }
    }
}