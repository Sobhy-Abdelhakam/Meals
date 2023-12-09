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
import coil.compose.AsyncImage
import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.presentation.UiState
import dev.sobhy.meals.ui.composable.ErrorScreen
import dev.sobhy.meals.util.MyAppScaffold

@Composable
fun FavAndSearchScreen(
    favAndSearchState: UiState<List<Meal>?>,
    favoriteMeals: () -> Unit,
    emptyingList: () -> Unit,
    searchMeals: (String) -> Unit,
    from: String?,
    mealItemClick: (Int) -> Unit,
) {
    LaunchedEffect(key1 = true) {
        if (from.equals("favorites")) {
            favoriteMeals()
        }
    }

    MyAppScaffold(
        title = if (from.equals("favorites")) from!! else "",
        actions = {
            if (from.equals("search")) {
                var textSearch by remember {
                    mutableStateOf("")
                }
                SearchTextField(
                    textSearch = textSearch,
                    onTextChange = {
                        textSearch = it
                        if (textSearch.isNotEmpty()) {
                            searchMeals(it)
                        } else {
                            emptyingList()
                        }
                    },
                )
            }
        },
    ) {
        Box(Modifier.padding(it)) {
            when (favAndSearchState) {
                is UiState.Error -> ErrorScreen(false) {}
                UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
                is UiState.Success -> {
                    val mealsList = favAndSearchState.data
                    if (from.equals("favorites")) {
                        NoFavoriteText(shouldTextShow = mealsList!!.isEmpty())
                    }
                    Box(modifier = Modifier.fillMaxSize()) {
                        mealsList?.let {
                            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                                items(it) { meal ->
                                    MealsItem(
                                        meal,
                                        onItemClick = {
                                            mealItemClick.invoke(meal.idMeal.toInt())
                                        },
                                    )
                                }
                            }
                        } ?: Text(
                            text = "Write the correct name of the meal",
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(
    textSearch: String,
    onTextChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = textSearch,
        onValueChange = onTextChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
            )
        },
        placeholder = {
            Text(text = "Search for meal")
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
    )
}

@Composable
fun MealsItem(meal: Meal, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onItemClick() },
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = meal.strMealThumb,
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            )
            Text(
                text = meal.strMeal,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally),
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun NoFavoriteText(shouldTextShow: Boolean) {
    if (shouldTextShow.not()) return
    Text(
        text = "There are no favorite meals",
        fontSize = 25.sp,
        modifier = Modifier.padding(16.dp),
    )
}
