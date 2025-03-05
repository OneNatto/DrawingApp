package com.example.drawingapp.ui.wordChain

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import com.example.drawingapp.ui.Drawing
import com.example.drawingapp.ui.model.WordChainDrawing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WordChainMainViewModel: ViewModel() {
    //初めの文字
    private val _firstHiragana = MutableStateFlow("")
    val firstHiragana: StateFlow<String> = _firstHiragana.asStateFlow()
    //書いた絵リスト
    private val _allDrawingList = MutableStateFlow<List<WordChainDrawing>>(emptyList())
    val allDrawingList: StateFlow<List<WordChainDrawing>> = _allDrawingList.asStateFlow()
    //絵描き中リスト
    private val _drawingList = MutableStateFlow<List<Path>>(emptyList())
    val drawingList: StateFlow<List<Path>> = _drawingList.asStateFlow()
    //絵描き中のPath
    private val _currentPath = MutableStateFlow(Path())
    val currentPath: StateFlow<Path> = _currentPath.asStateFlow()

    // パスの作成開始
    fun onDragStart(startPoint: Offset) {
        _currentPath.value = Path().apply {
            moveTo(startPoint.x, startPoint.y)
        }
    }

    init {
        setFirstHiragana()
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
        newList.add(_currentPath.value)

        _drawingList.value = newList
        _currentPath.value = Path()
    }

    fun setFirstHiragana() {
        val hiraganaList = listOf(
            'あ', 'い', 'う', 'え', 'お', // あ行
            'か', 'き', 'く', 'け', 'こ', // か行
            'さ', 'し', 'す', 'せ', 'そ', // さ行
            'た', 'ち', 'つ', 'て', 'と', // た行
            'な', 'に', 'ぬ', 'ね', 'の', // な行
            'は', 'ひ', 'ふ', 'へ', 'ほ', // は行
            'ま', 'み', 'む', 'め', 'も', // ま行
            'や', 'ゆ', 'よ', // や行
            'ら', 'り', 'る', 'れ', 'ろ', // ら行
            'わ' // わ行
        ).map { it.toString() }

        _firstHiragana.value = hiraganaList.random()
    }

    fun updateAllDrawingList() {
        val newWordChainDrawing = WordChainDrawing(
            drawingList = _drawingList.value
        )

        val newList = _allDrawingList.value.toMutableList()
        newList.add(newWordChainDrawing)

        _allDrawingList.value = newList
        _drawingList.value = emptyList()
    }
}