package com.example.drawingapp.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

data class Drawing(
    val path: Path,
    val penColor: Color,
    val penStrokeWidth: Float
)
