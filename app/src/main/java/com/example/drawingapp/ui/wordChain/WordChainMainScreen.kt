package com.example.drawingapp.ui.wordChain

import android.app.Dialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
    val answer by viewModel.answer.collectAsState()
    val showDialog by viewModel.isDialogShow.collectAsState()

    if (showDialog) {
        AnswerDialog(
            answer = answer,
            updateAnswer = {
                viewModel.enterAnswer(it)
            },
            onConfirmClicked = {
                viewModel.addNewDrawingToDB()
            },
            onDismissClicked = {
                viewModel.toggleIsDialogShow()
            }
        )
    }

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
                            .weight(1F)
                    )
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                    ) {
                        items(allDrawingList.size + 1) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(Color(0xFFFB6542), shape = RoundedCornerShape(5.dp))
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
                viewModel.toggleIsDialogShow()
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
            val answer = allDrawingList[page - 1].answer.length
            val drawingPathList = allDrawingList[page - 1].drawing
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    for (i in 0 until answer) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(30.dp)
                                .border(2.dp, Color(0xFFDE7A22),shape = RoundedCornerShape(10.dp))
                                .background(Color(0xFFFFBB00),shape = RoundedCornerShape(10.dp))
                        ) {
                            Text(
                                "?",
                                fontSize = 20.sp,
                                color = Color.DarkGray
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth(0.9F)
                        .weight(1F)
                    // .border(BorderStroke(2.dp, Color.Blue))
                ) {
                    if (drawingPathList.isNotEmpty()) {
                        drawingPathList.forEach { path ->
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

@Composable
fun AnswerDialog(
    answer: String,
    updateAnswer: (String) -> Unit,
    onConfirmClicked: () -> Unit,
    onDismissClicked: () -> Unit,
) {
    Dialog(
        onDismissRequest = {
            onDismissClicked()
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9F)
                .fillMaxHeight(0.25F)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxSize()
            ) {
                TextField(
                    value = answer,
                    onValueChange = {
                        updateAnswer(it)
                    },
                    label = {
                        Text("答えをひらがなで入力してください")
                    },
                    modifier = Modifier.fillMaxWidth(0.9F)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            onDismissClicked()
                        }
                    ) {
                        Text("キャンセル")
                    }
                    TextButton(
                        onClick = {
                            onConfirmClicked()
                        }
                    ) {
                        Text("決定")
                    }
                }
            }
        }
    }
}