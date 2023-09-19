package dev.sobhy.meals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.sobhy.meals.navigation.Screens
import dev.sobhy.meals.navigation.SetupNavGraph
import dev.sobhy.meals.ui.theme.MealsTheme
import dev.sobhy.meals.util.AppBarState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.splashCondition
            }
        }
        setContent {
            MealsTheme {
                var appBarState by remember {
                    mutableStateOf(AppBarState())
                }
                Scaffold(
                    topBar = {
                        TopBarWithMutableContent(appBarState = appBarState)
                    }
                ) {
                    Column(modifier = Modifier.padding(it)) {
                        val navController = rememberNavController()
                        val startDestination = viewModel.startDestination
                        SetupNavGraph(
                            navController = navController,
                            startDestination = startDestination,
                            changeAppBarState = {state ->
                                appBarState = state
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithMutableContent(appBarState: AppBarState) {
    if (!appBarState.show) return
    TopAppBar(
        title = {
            Text(
                text = appBarState.title,
                fontSize = 32.sp,
                fontFamily = FontFamily(Font(R.font.charm_bold)),
                modifier = Modifier.padding(2.dp)
            )
        },
        actions = {
            appBarState.actions?.invoke(this)
        }
    )
}
