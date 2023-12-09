package dev.sobhy.meals.presentation.onboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.HorizontalPagerIndicator
import dev.sobhy.meals.util.OnBoardingPage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreen(
    skipClick: () -> Unit,
    getStartButton: () -> Unit,
) {
    val pages = listOf(
        OnBoardingPage.First,
        OnBoardingPage.Second,
        OnBoardingPage.Third,
        OnBoardingPage.Fourth,
    )
    val pagerState = rememberPagerState { 4 }
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Skip",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(32.dp)
                .align(Alignment.End)
                .clickable {
                    skipClick.invoke()
                },
        )
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.weight(9f),
        ) { position ->
            PagerScreen(onBoardingPage = pages[position])
        }
        // horizontal pager indicator 👇

        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = 4,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f),
        )
        GetStartedButton(
            modifier = Modifier.weight(1f),
            pagerState = pagerState,
        ) {
            getStartButton.invoke()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GetStartedButton(
    modifier: Modifier,
    pagerState: PagerState,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.padding(horizontal = 40.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center,
    ) {
        AnimatedVisibility(
            visible = pagerState.currentPage == 3,
            modifier = modifier.fillMaxWidth(),
        ) {
            Button(onClick = onClick) {
                Text(text = "GetStarted")
            }
        }
    }
}

@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(onBoardingPage.image))
        val progress by animateLottieCompositionAsState(composition = composition)
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.7f),
        )

        Text(
            text = onBoardingPage.title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = onBoardingPage.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(top = 20.dp),
        )
    }
}
