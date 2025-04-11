package com.example.drawingapp.ui.wordChain

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
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


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(gameDrawing) { index,it ->
            Card(
                modifier = Modifier.size(200.dp)
            ) {
                val pathList = it.drawing
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height


                    pathList.forEach { drawing ->
                        val bounds = drawing.getBounds()
                        val pathWidth = bounds.width
                        val pathHeight = bounds.height

                        // Canvasに対するスケール値を動的に計算
                        val scaleX =
                            if (pathWidth > 0) canvasWidth / pathWidth * 0.7F else 1F
                        val scaleY =
                            if (pathHeight > 0) canvasHeight / pathHeight * 0.7F else 1F
                        val scale = minOf(scaleX, scaleY)

                        // 中央配置のためのOffsetを計算
                        val offsetX = -bounds.left
                        val offsetY = -bounds.top
                        Log.d("CanvasScreen", "pathWidth: ${pathWidth} pathHeight: ${pathHeight}")
                        Log.d(
                            "CanvasScreen",
                            "canvasWidth: ${canvasWidth} canvasHeight: ${canvasHeight}"
                        )
                        Log.d("CanvasScreen", "scaleX: ${scaleX} scaleY: ${scaleY}")
                        Log.d("CanvasScreen", "offsetX: ${offsetX} offsetY: ${offsetY}")

                        translate(offsetX, offsetY) {
                            scale(scale, scale) {
                                drawPath(
                                    path = drawing,
                                    color = Color.Black,
                                    style = Stroke(3f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}