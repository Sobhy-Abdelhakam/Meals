package dev.sobhy.meals.presentation.home

import android.content.Context
import android.net.ConnectivityManager
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
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import dev.sobhy.mealzapp.ui.composable.Loader
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavHostController) {
    val state by viewModel.homeState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var loop by remember { mutableStateOf(false) }

    DisposableEffect(context) {
        // executed when the composable is first composed
        loop = true
        coroutineScope.launch {
            // asynchronous initialization logic
            while (loop) {
                Log.e("homeScreen", "this in loop")
                viewModel.getRandomMeal()
                delay(5000)
            }
        }
        onDispose {
            // executed when the composable is removed from the composition
            loop = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(
                rememberScrollState(),
                orientation = Orientation.Vertical,
                enabled = true
            )
    ) {
        Box { Loader(shouldShow = state.isLoading) }
        RandomMealCard(state = state)

        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Categories", fontWeight = FontWeight.Bold, modifier = Modifier.padding(6.dp))
        CategoriesLazyGrid(state = state, navController = navController)

        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Areas", fontWeight = FontWeight.Bold, modifier = Modifier.padding(6.dp))
        AreasLazyGrid(state = state, navController = navController)
    }
}

@Composable
fun RandomMealCard(state: HomeState) {
    Card(modifier = Modifier.padding(8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f)
        ) {
            state.mealDetails?.let {
                Log.e("HomeScreen", state.mealDetails.toString())
                AsyncImage(
                    model = it.strMealThumb,
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = it.strMeal,
                    fontSize = 25.sp,
                    color = Color.Black,
//                        style = LocalTextStyle.current.merge(
//                            TextStyle(
//                                color = Color.White,
//                                drawStyle = Stroke(width = 5f)
//                            )
//                        ),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun CategoriesLazyGrid(state: HomeState, navController: NavHostController) {
    LazyHorizontalGrid(
        contentPadding = PaddingValues(8.dp),
        rows = GridCells.Fixed(2),
        modifier = Modifier.fillMaxHeight(0.5f)
    ) {
        items(state.categoryResponse) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clickable {
                        navController.navigate("meals_list/category/${it.strCategory}")
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    model = it.strCategoryThumb,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                )
                Text(text = it.strCategory)
            }
        }
    }
}

@Composable
fun AreasLazyGrid(state: HomeState, navController: NavHostController) {
    LazyHorizontalGrid(
        contentPadding = PaddingValues(8.dp),
        rows = GridCells.Fixed(3),
    ) {
        items(state.areasResponse) {
            OutlinedButton(
                onClick = {
                    navController.navigate("meals_list/area/${it.strArea}")
                },
                modifier = Modifier
                    .padding(6.dp)
                    .width(125.dp)
            ) {
                Text(text = it.strArea)
            }
        }
    }
}

fun isNetworkConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}