package dev.sobhy.meals.presentation.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.sobhy.meals.ui.composable.Loader
import dev.sobhy.meals.util.MyAppScaffold
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    state: HomeState,
    getRandomMeal: () -> Unit,
    searchIconClick: () -> Unit,
    favoriteIconClick: () -> Unit,
    randomMealClick: (Int) -> Unit,
    areasMealClick: (String) -> Unit,
    categoriesMealsClick: (String) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val job = remember { mutableStateOf<Job?>(null) }

    DisposableEffect(key1 = context) {
        job.value = coroutineScope.launch {
            while (isActive) {
                getRandomMeal()
                delay(5000)
                Log.e("HomeScreenDisposable: ", "DisposableEffect")
            }
        }
        onDispose {
            job.value?.cancel()
        }
    }

    MyAppScaffold(
        title = "Meals",
        actions = {
            ActionIcon(
                iconClick = searchIconClick,
                icon = Icons.Filled.Search,
                contentDescription = "Search",
            )
            ActionIcon(
                iconClick = favoriteIconClick,
                icon = Icons.Filled.Favorite,
                contentDescription = "Favorite",
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .scrollable(
                    rememberScrollState(),
                    orientation = Orientation.Vertical,
                    enabled = true,
                ),
        ) {
            Box { Loader(shouldShow = state.isLoading) }
            RandomMealCard(state = state, randomMealClick = randomMealClick, context = context)

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Categories", fontWeight = FontWeight.Bold, modifier = Modifier.padding(6.dp))
            CategoriesLazyGrid(state = state, categoriesMealsClick = categoriesMealsClick)

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Areas", fontWeight = FontWeight.Bold, modifier = Modifier.padding(6.dp))
            AreasLazyGrid(state = state, areasMealClick = areasMealClick)
        }
    }
}

@Composable
fun ActionIcon(
    iconClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
) {
    IconButton(onClick = iconClick) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier
                .padding(6.dp),
        )
    }
}

@Composable
fun RandomMealCard(state: HomeState, randomMealClick: (Int) -> Unit, context: Context) {
    Card(modifier = Modifier.padding(8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f),
        ) {
            if (isNetworkConnected(context).not()) {
                Text(
                    text = "No internet connection",
                    fontSize = 23.sp,
                    modifier = Modifier.align(Alignment.Center),
                )
            } else {
                state.mealDetails?.let {
                    AsyncImage(
                        model = it.strMealThumb,
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                randomMealClick.invoke(it.idMeal.toInt())
                            },
                    )
                    Text(
                        text = it.strMeal,
                        fontSize = 23.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun CategoriesLazyGrid(state: HomeState, categoriesMealsClick: (String) -> Unit) {
    LazyHorizontalGrid(
        contentPadding = PaddingValues(8.dp),
        rows = GridCells.Fixed(2),
        modifier = Modifier.fillMaxHeight(0.5f),
    ) {
        items(state.categoryResponse) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clickable {
                        categoriesMealsClick.invoke(it.strCategory)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    model = it.strCategoryThumb,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape),
                )
                Text(text = it.strCategory)
            }
        }
    }
}

@Composable
fun AreasLazyGrid(state: HomeState, areasMealClick: (String) -> Unit) {
    LazyHorizontalGrid(
        contentPadding = PaddingValues(8.dp),
        rows = GridCells.Fixed(3),
    ) {
        items(state.areasResponse) {
            OutlinedButton(
                onClick = {
                    areasMealClick.invoke(it.strArea)
                },
                modifier = Modifier
                    .padding(6.dp)
                    .width(125.dp),
            ) {
                Text(text = it.strArea)
            }
        }
    }
}

fun isNetworkConnected(context: Context): Boolean {
    val connectivityManager = context
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}
