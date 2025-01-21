package com.example.drawingapp.ui

import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CanvasViewModel : ViewModel() {
    //書いた絵リスト
    private val _drawingList = MutableStateFlow<List<Drawing>>(emptyList())
    val drawingList: StateFlow<List<Drawing>> = _drawingList.asStateFlow()

    private val _currentPath = MutableStateFlow(Path())
    val currentPath: StateFlow<Path> = _currentPath.asStateFlow()

    // ペンの色と太さの状態
    private val _penColor = MutableStateFlow(Color.Black)
    val penColor: StateFlow<Color> = _penColor.asStateFlow()

    private val _penStrokeWidth = MutableStateFlow(5f) // 初期の太さを5fに設定
    val penStrokeWidth: StateFlow<Float> = _penStrokeWidth.asStateFlow()

    // パスの作成開始
    fun onDragStart(startPoint: Offset) {
        _currentPath.value = Path().apply {
            moveTo(startPoint.x, startPoint.y)
        }
    }

    //絵描き中
    fun onDrag(point: Offset) {
        val updatePath = Path().apply {
            addPath(_currentPath.value)
            lineTo(point.x, point.y)
        }

        _currentPath.value = updatePath
    }

    fun onDragEnd() {
        val newList = _drawingList.value.toMutableList()
        val newDrawing = Drawing(
            path = _currentPath.value,
            penColor = _penColor.value,
            penStrokeWidth = _penStrokeWidth.value
        )
        newList.add(newDrawing)

        _drawingList.value = newList
        _currentPath.value = Path()
    }

    fun onColorChanged(newColor: Color) {
        _penColor.value = newColor
        _currentPath.value = Path()
    }

    fun onStrokeWidthChange(strokeWidth: Float) {
        _penStrokeWidth.value = strokeWidth
        _currentPath.value = Path()
    }

    fun onClear() {
        _drawingList.value = emptyList()
        _currentPath.value = Path()
    }

    @RequiresApi(35)
    fun onStepBack() {
        if (_drawingList.value.isNotEmpty()) {
            val drawingList = _drawingList.value.toMutableList()
            drawingList.removeLast()
            _drawingList.value = drawingList
        }
    }
}