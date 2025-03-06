package com.example.drawingapp.ui.wordChain

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    DrawingListPager(
                        firstHiragana,
                        allDrawingList,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.9F)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                    ) {
                        items(allDrawingList.size + 1) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .border(4.dp, Color.Black, CircleShape)
                                    .background(Color.Black)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
            }
        }
        DrawingArea(
            viewModel,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
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

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8F)
                .fillMaxHeight(0.8F)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                viewModel.onDragStart(offset)
                            },
                            onDragEnd = {
                                viewModel.onDragEnd()
                            },
                            onDrag = { change, _ ->
                                if (change.position.y > 0) {
                                    viewModel.onDrag(change.position)
                                }
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
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                viewModel.updateAllDrawingList()
            },
            modifier = Modifier.fillMaxWidth(0.8F)
        ) {
            Text(
                "描きおわり",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
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
        verticalAlignment = Alignment.CenterVertically,
        state = pagerState,
        modifier = modifier
    ) { page ->
        if (page == 0) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Text(
                    firstHiragana,
                    fontSize = 70.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth(0.9F)
                        .fillMaxHeight(0.9F)
                        .border(BorderStroke(2.dp, Color.Blue))
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
}