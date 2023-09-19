package dev.sobhy.meals.util

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

data class AppBarState(
    val show: Boolean = false,
    val title: String = "",
    val actions: (@Composable RowScope.() -> Unit)? = null
)
