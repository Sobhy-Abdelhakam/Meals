package dev.sobhy.meals.presentation.favoriteandsearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.util.AppBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavAndSearchScreen(
    favAndSearchViewModel: FavAndSearchViewModel,
    from: String?,
    navController: NavHostController,
    onComposing: (AppBarState) -> Unit
) {
    if (from.equals("favorites")) {
        favAndSearchViewModel.getFavoriteMeals()
    }

    var textSearch by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                show = true,
                title = if (from.equals("favorites")) from!! else "",
                actions = {
                    if (from.equals("search")) {
                        OutlinedTextField(
                            value = textSearch,
                            onValueChange = {
                                textSearch = it
                                if (textSearch.isNotEmpty()) {
                                    favAndSearchViewModel
                                        .getMealsContainString(textSearch)
                                } else{
                                    favAndSearchViewModel.makeListEmpty()
                                }
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Icon"
                                )
                            },
                            placeholder = {
                                Text(text = "Search for meal")
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp)
                        )
                    }
                }
            )
        )
    }

    val favAndSearchState by favAndSearchViewModel.favState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Loading(
            showLoader = favAndSearchState.searchFavLoading,
            modifier = Modifier.align(Alignment.Center)
        )

        if (favAndSearchState.responseMealsList.isEmpty() && from.equals("favorites")) {
            Text(
                text = "There are no favorite meals",
                fontSize = 25.sp,
                modifier = Modifier.padding(16.dp)
            )
        }


        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(favAndSearchState.responseMealsList) { meal ->
                MealsItem(
                    meal,
                    onItemClick = {
                        navController.navigate("meal_details/${meal.idMeal.toInt()}")
                    }
                )
            }
        }

    }
}

@Composable
fun MealsItem(meal: Meal, onItemClick: () -> Unit) {
    Card(modifier = Modifier
        .padding(8.dp)
        .clickable { onItemClick() }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = meal.strMealThumb,
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Text(
                text = meal.strMeal,
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

@Composable
fun Loading(showLoader: Boolean, modifier: Modifier) {
    if (showLoader.not()) return
    CircularProgressIndicator(modifier = modifier)
}