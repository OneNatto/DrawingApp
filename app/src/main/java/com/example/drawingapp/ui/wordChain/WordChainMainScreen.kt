package com.example.drawingapp.ui.wordChain

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.drawingapp.ui.model.WordChainDrawing

@Composable
fun WordChainMainScreen(
    viewModel: WordChainMainViewModel = viewModel(),
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val allDrawingList by viewModel.allDrawingList.collectAsState()
    val firstHiragana by viewModel.firstHiragana.collectAsState()
    Column(
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
        ) {
            if (allDrawingList.isEmpty()) {
                Text(
                    firstHiragana,
                    fontSize = 70.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                DrawingListPager(
                    firstHiragana,
                    allDrawingList,
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5F)
                )
            }
        }
        DrawingArea(
            viewModel,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        )
    }
}


@Composable
fun DrawingArea(
    viewModel: WordChainMainViewModel,
    modifier: Modifier = Modifier
) {
    val currentPath by viewModel.currentPath.collectAsState()
    val drawingList by viewModel.drawingList.collectAsState()

    Box(
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5F)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            viewModel.onDragStart(offset)
                        },
                        onDragEnd = {
                            viewModel.onDragEnd()
                        },
                        onDrag = { change, _ ->
                            viewModel.onDrag(change.position)
                        }
                    )
                }
        ) {
            if (drawingList.isNotEmpty()) {
                drawingList.forEach { path ->
                    drawPath(
                        path = path,
                        color = Color.Black,
                        style = Stroke(3f)
                    )
                }
            }
            drawPath(
                path = currentPath,
                color = Color.Black,
                style = Stroke(3f)
            )
        }
        Button(
            onClick = {
                viewModel.updateAllDrawingList()
            }
        ) {
            Text("描きおわり")
        }
    }
}

@Composable
fun DrawingListPager(
    firstHiragana: String,
    allDrawingList: List<WordChainDrawing>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        pageCount = { allDrawingList.size + 1 }
    )
    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->
        if (page == 0) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5F)
            ) {
                Text(
                    firstHiragana,
                    fontSize = 70.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Canvas(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (allDrawingList[page - 1].drawingList.isNotEmpty()) {
                    allDrawingList[page - 1].drawingList.forEach { path ->
                        drawPath(
                            path = path,
                            color = Color.Black,
                            style = Stroke(3f)
                        )
                    }
                }
            }
        }
    }
}