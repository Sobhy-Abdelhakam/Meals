package dev.sobhy.meals.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SignalWifiConnectedNoInternet4
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.sobhy.meals.R

@Composable
fun ErrorScreen(
    showRefreshButton: Boolean = true,
    refreshClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_connection),
            contentDescription = "no Internet",
            modifier = Modifier
                .size(200.dp)
                .padding(32.dp)
        )
        Text(
            text = "No Internet Connection",
            fontSize = 25.sp,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Check your connection. then refresh the page",
            modifier = Modifier.padding(8.dp)
        )

        if(showRefreshButton){
            OutlinedButton(
                onClick = { refreshClick() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Refresh")
            }
        }

    }

}