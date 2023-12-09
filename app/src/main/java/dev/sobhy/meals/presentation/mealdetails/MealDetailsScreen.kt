package dev.sobhy.meals.presentation.mealdetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dev.sobhy.meals.domain.model.meal.Meal
import dev.sobhy.meals.presentation.UiState
import dev.sobhy.meals.ui.composable.ErrorScreen
import dev.sobhy.meals.ui.composable.Loader
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import kotlin.reflect.full.memberProperties

@Composable
fun MealDetailsScreen(
    state: UiState<Meal>,
    onFavoriteIconClick: (Meal) -> Unit,
    onRefreshButtonClick: () -> Unit,
    onBackButtonClick: () -> Unit,
) {
    val context = LocalContext.current

    when (state) {
        is UiState.Loading -> Box { Loader() }
        is UiState.Success -> {
            val meal = state.data
            DetailsScreenContent(
                meal = meal,
                onFavoriteIconClick = {
                    onFavoriteIconClick(it)
                },
                onBackButtonClick = onBackButtonClick,
                context = context,
            )
        }

        is UiState.Error -> ErrorScreen {
            onRefreshButtonClick()
        }
    }
}

@Composable
fun DetailsScreenContent(
    meal: Meal,
    onFavoriteIconClick: (Meal) -> Unit,
    onBackButtonClick: () -> Unit,
    context: Context,
) {
    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxWidth(),
        state = rememberCollapsingToolbarScaffoldState(),
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        toolbar = {
            ToolbarContent(
                meal = meal,
                onFavoriteIconClick = {
                    onFavoriteIconClick(it)
                },
                onBackButtonClick = onBackButtonClick,
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            ContentHeader(meal, context)
            Card(
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                ) {
                    Text(
                        text = meal.strMeal,
                        fontSize = 25.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                    MealContentSection(title = "- Ingredients") {
                        IngredientsScopeContent(meal = meal)
                    }
                    MealContentSection(title = "- Instructions") {
                        Text(
                            text = meal.strInstructions,
                            modifier = Modifier.padding(8.dp),
                            textAlign = TextAlign.Center,
                        )
                    }
                    MealContentSection(title = "- Watch meal prep") {
                        if (meal.strYoutube.isNotEmpty()) {
                            YoutubeScreen(videoUrl = meal.strYoutube)
                        } else {
                            Text(
                                text = "There is no video for this meal",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MealContentSection(title: String, content: @Composable () -> Unit) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp),
    )
    content()
}

@Composable
fun ContentHeader(meal: Meal, context: Context) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row {
            Icon(
                imageVector = Icons.Filled.Fastfood,
                contentDescription = "Category Icon",
            )
            Text(
                text = meal.strCategory,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp),
            )
        }
        Row {
            Icon(
                imageVector = Icons.Outlined.Flag,
                contentDescription = "Area Icon",
            )
            Text(
                text = meal.strArea,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp),
            )
        }
        IconButton(onClick = {
            if (meal.strYoutube.isNotEmpty()) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(meal.strYoutube),
                    ),
                )
            }
        }) {
            Icon(
                imageVector = Icons.Filled.SmartDisplay,
                contentDescription = "YouTube Icon",
                modifier = Modifier.size(40.dp),
                tint = Color.Red,
            )
        }
    }
}

@Composable
fun ToolbarContent(
    meal: Meal,
    onFavoriteIconClick: (Meal) -> Unit,
    onBackButtonClick: () -> Unit,
) {
    AsyncImage(
        model = meal.strMealThumb,
        contentDescription = meal.strMeal,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
    )
    ToolbarActionIcons(
        isFavorite = meal.isFavorite,
        onFavoriteIconClick = { onFavoriteIconClick(meal) },
        onBackButtonClick = onBackButtonClick,
    )
}

@Composable
fun ToolbarActionIcons(
    isFavorite: Boolean,
    onFavoriteIconClick: () -> Unit,
    onBackButtonClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ToolbarIcons(onIconClick = onBackButtonClick, icon = Icons.Rounded.ArrowBack)

        ToolbarIcons(
            onIconClick = onFavoriteIconClick,
            icon = Icons.Filled.Favorite,
            tint = if (isFavorite) Color.Red else Color.LightGray,
        )
    }
}

@Composable
fun ToolbarIcons(
    onIconClick: () -> Unit,
    icon: ImageVector,
    tint: Color = LocalContentColor.current,
) {
    IconButton(onClick = { onIconClick() }) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.background, shape = CircleShape)
                .padding(8.dp),
        )
    }
}

@Composable
fun IngredientsScopeContent(meal: Meal) {
    for (indexOfForLoop in 1..20 step (4)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            repeat(4) { repeatIndex ->
                // reflection using kotlin
                val ingredientValue = Meal::class.memberProperties
                    .find {
                        it.name == "strIngredient${indexOfForLoop + repeatIndex}"
                    }?.get(meal) as? String

                val measure = Meal::class.memberProperties.find {
                    it.name == "strMeasure${indexOfForLoop + repeatIndex}"
                }?.get(meal) as? String

                // Check if the ingredient value is empty before using it
                // if the ingredient is empty, this mean that all ingredients after that is empty
                if (ingredientValue.isNullOrBlank()) return

                Card(modifier = Modifier.width(80.dp)) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        AsyncImage(
                            model = "https://www.themealdb.com/images/ingredients/$ingredientValue.png",
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(80.dp),
                        )
                        Text(
                            text = "$measure $ingredientValue",
                            modifier = Modifier
                                .padding(4.dp)
                                .align(Alignment.CenterHorizontally),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun YoutubeScreen(videoUrl: String) {
    val videoId = videoUrl.substring(videoUrl.indexOf("v=") + 2)
    AndroidView(factory = {
        val view = YouTubePlayerView(it)
        view.addYouTubePlayerListener(
            object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            },
        )
        view
    })
}
