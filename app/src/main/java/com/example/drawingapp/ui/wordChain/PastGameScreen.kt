package com.example.drawingapp.ui.wordChain

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun PastGameScreen(
    gameId: String,
    navController: NavController,
    viewModel: PastGameViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val gameDrawing by viewModel.gameDrawing.collectAsState()

    LaunchedEffect(gameId) {
        viewModel.getGameDrawings(gameId)
    }

    // ここにコードを追加
    Column(
        modifier = modifier
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(gameDrawing) {
                Card(
                    modifier = Modifier.size(100.dp)
                ) {
                    Column {
                        val pathList = it.drawing
                        Canvas(
                            modifier = Modifier.size(100.dp)
                        ) {
                            pathList.forEach { drawing ->
                                drawPath(
                                    path = drawing,
                                    color = Color.Black,
                                    style = Stroke(3f)
                                )
                            }
                        }
                        Text(it.answer)
                        Text(it.userId)
                    }
                }
            }
        }
    }
}