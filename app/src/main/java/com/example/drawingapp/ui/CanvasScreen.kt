package com.example.drawingapp.ui

import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@RequiresApi(35)
@Composable
fun CanvasScreen(
    canvasViewModel: CanvasViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    //書いた絵リスト
    val drawingList by canvasViewModel.drawingList.collectAsState()
    //絵書き中のPath
    val currentPath by canvasViewModel.currentPath.collectAsState()
    //ペンの色
    val penColor by canvasViewModel.penColor.collectAsState()
    //ペンの太さ
    val penStrokeWidth by canvasViewModel.penStrokeWidth.collectAsState()

    Column(
        modifier = modifier
    ) {
        DrawingArea(
            drawingList,
            currentPath,
            penColor,
            penStrokeWidth,
            canvasViewModel,
            modifier = Modifier.weight(1F)
        )
        ControlBar(
            onColorChange = {
                canvasViewModel.onColorChanged(it)
            },
            onStrokeWidthChange = {
                canvasViewModel.onStrokeWidthChange(it)
            },
            onClear = {
                canvasViewModel.onClear()
            },
            onStepBack = {
                canvasViewModel.onStepBack()
            },
            currentStrokeWidth = penStrokeWidth,
        )
    }
}


@Composable
fun DrawingArea(
    drawingList: List<Drawing>,
    currentPath: Path,
    penColor: Color,
    penStrokeWidth: Float,
    canvasViewModel: CanvasViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            canvasViewModel.onDragStart(offset)
                        },
                        onDragEnd = {
                            canvasViewModel.onDragEnd()
                        },
                        onDrag = { change, _ ->
                            canvasViewModel.onDrag(change.position)
                        }
                    )
                }
        ) {
            if (drawingList.isNotEmpty()) {
                drawingList.forEach { drawing ->
                    drawPath(
                        path = drawing.path,
                        color = drawing.penColor,
                        style = Stroke(drawing.penStrokeWidth)
                    )
                }
            }
            drawPath(
                path = currentPath,
                color = penColor,
                style = Stroke(penStrokeWidth)
            )
        }
    }
}

@Composable
fun ControlBar(
    onColorChange: (Color) -> Unit,
    onStrokeWidthChange: (Float) -> Unit,
    onClear: () -> Unit,
    onStepBack: () -> Unit,
    currentStrokeWidth: Float
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // 色の選択
        Text(
            "ペンの色を選択",
            style = MaterialTheme.typography.bodyMedium
        )

        Row(
            modifier = Modifier
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 色選択ボタン（サンプルとして3つの色）
            ColorOptionButton(
                color = Color.Black,
                onClick = {
                    onColorChange(Color.Black)
                }
            )
            ColorOptionButton(
                color = Color.Red,
                onClick = {
                    onColorChange(Color.Red)
                }
            )
            ColorOptionButton(
                color = Color.Blue,
                onClick = {
                    onColorChange(Color.Blue)
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onStepBack
                ) {
                    Text(
                        "前に戻す",
                        fontSize = 12.sp
                    )
                }
                Button(
                    onClick = onClear
                ) {
                    Text(
                        "クリア",
                        fontSize = 12.sp
                    )
                }
            }
        }

        // ペンの太さのスライダー
        Text(
            "ペンの太さ",
            style = MaterialTheme.typography.bodyMedium
        )
        Slider(
            value = currentStrokeWidth,
            onValueChange = { onStrokeWidthChange(it) },
            valueRange = 1f..20f, // 太さの範囲
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun ColorOptionButton(
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color, CircleShape)
            .clickable(onClick = onClick)
    )
}