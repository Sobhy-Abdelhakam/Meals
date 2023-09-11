package dev.sobhy.meals.presentation.mealdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.ui.theme.md_theme_light_secondaryContainer
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailsScreen(mealViewModel: MealDetailsViewModel, mealId: Int?) {
    mealViewModel.getMeal(mealId!!)
    val state by mealViewModel.mealState.collectAsState()
    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxWidth(),
        state = rememberCollapsingToolbarScaffoldState(),
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        toolbar = {
            state.meal?.let {
                AsyncImage(
                    model = it.strMealThumb,
                    contentDescription = it.strMeal,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White, shape = CircleShape)
                            .padding(8.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "",
                        tint = Color.Red,
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White, shape = CircleShape)
                            .padding(8.dp)
                    )
                }
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(md_theme_light_secondaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Filled.Fastfood,
                        contentDescription = "Category Icon"
                    )
                    Text(
                        text = state.meal?.strCategory ?: "",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Flag,
                        contentDescription = "Area Icon",
                    )
                    Text(
                        text = state.meal?.strArea ?: "",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Filled.SmartDisplay,
                    contentDescription = "YouTube Icon",
                    modifier = Modifier.size(40.dp),
                    tint = Color.Red
                )
            }
            Card(
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Text(
                        text = state.meal?.strMeal ?: "",
                        fontSize = 25.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Text(text = "- Ingredients", color = Color.Black)
                    state.meal?.let {meal ->
                        val meal = meal
                        repeat(20) {
                            val ingredientProperty = Meal::class.java.getDeclaredField("strIngredient${it+1}")
                            ingredientProperty.isAccessible = true
                            val ingredientValue = ingredientProperty.get(meal) as? String

                            val measureProp = Meal::class.java.getDeclaredField("strMeasure${it+1}")
                            measureProp.isAccessible = true
                            val measure = measureProp.get(meal) as? String

                            // Check if the ingredient value is not null or empty before using it
                            if (!ingredientValue.isNullOrBlank()) {

                                Text(text = "${it + 1}- $measure $ingredientValue")
                            }
                        }
                    }

                }
            }
        }
    }
}

//@Preview
//@Composable
//fun MealDetailScreen() {
//    val scrollState = rememberScrollState()
//
//    val maxImageHeight = with(LocalDensity.current) { 100.dp.toPx().roundToInt() }
//
//    var imageScaleFactor by remember {
//        mutableStateOf(1f)
//    }
//
//    LaunchedEffect(scrollState.value) {
//        val offset = scrollState.value
//        imageScaleFactor = 1f - (offset / maxImageHeight)
//    }
//
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(state = scrollState)
//    ) {
//        // Cover Image
//        Box(modifier = Modifier.fillMaxWidth()) {
//            Icon(
//                imageVector = Icons.Filled.ArrowBack,
//                contentDescription = "",
//                modifier = Modifier.align(Alignment.TopStart)
//            )
//            Icon(
//                imageVector = Icons.Filled.FavoriteBorder,
//                contentDescription = "",
//                modifier = Modifier.align(Alignment.TopEnd)
//            )
//        }
//        Image(
//            painter = painterResource(id = R.drawable.landscape), // Replace with your image resource
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height((maxImageHeight * imageScaleFactor).dp)
//        )
//        repeat(100){
//            Text(text = "Pla pla pla pla pla")
//        }
//
//    }
//}