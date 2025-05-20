package com.example.drawingapp.ui.wordChain.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.drawingapp.ui.navigation.Routes
import com.example.drawingapp.ui.wordChain.viewmodel.WaitingRoomViewModel

@Composable
fun WaitingRoomScreen(
    navController: NavController,
//    viewModel: WaitingRoomViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "ゲーム部屋",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Column {
            Text(
                text = "user1"
            )
            Text(
                text = "user2"
            )
            Text(
                text = "user3"
            )
        }
        Button(
            onClick = {
                navController.navigate(Routes.WordChainMainScreen.route)
            },
            modifier = Modifier
        ) {
            Text(
                text = "ゲーム開始"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WaitingRoomScreenPreview() {
    val navController = rememberNavController()

    WaitingRoomScreen(
        navController = navController,
        modifier = Modifier.fillMaxSize()
    )
}
