package com.example.drawingapp.ui.wordChain

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.lifecycle.ViewModel
import com.example.drawingapp.ui.model.WordChainDrawing
import com.example.drawingapp.utils.PathConverter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WordChainMainViewModel : ViewModel() {
    // ゲームのID
    private val _currentGameId = MutableStateFlow("")
    val currentGameId: StateFlow<String> = _currentGameId.asStateFlow()

    // 初めの文字
    private val _firstHiragana = MutableStateFlow("")
    val firstHiragana: StateFlow<String> = _firstHiragana.asStateFlow()

    // 回答
    private val _answer = MutableStateFlow("")
    val answer: StateFlow<String> = _answer.asStateFlow()

    // 書いた絵リスト
    private val _allDrawingList = MutableStateFlow<List<WordChainDrawing>>(emptyList())
    val allDrawingList: StateFlow<List<WordChainDrawing>> = _allDrawingList.asStateFlow()

    // 絵描き中リスト
    private val _drawingList = MutableStateFlow<List<Path>>(emptyList())
    val drawingList: StateFlow<List<Path>> = _drawingList.asStateFlow()

    // 絵描き中のPath
    private val _currentPath = MutableStateFlow(Path())
    val currentPath: StateFlow<Path> = _currentPath.asStateFlow()

    // ダイアログフラグ
    private val _isDialogShow = MutableStateFlow(false)
    val isDialogShow: StateFlow<Boolean> = _isDialogShow.asStateFlow()

    val database: FirebaseFirestore = FirebaseFirestore.getInstance()

    // パスの作成開始
    fun onDragStart(startPoint: Offset) {
        _currentPath.value = Path().apply {
            moveTo(startPoint.x, startPoint.y)
        }
    }

    init {
        startGame()
    }

    // 絵描き中
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

    fun startGame() {
        setFirstHiragana()
        // コレクションを取得
        val collection = database.collection("word_chain_drawing")
        // ドキュメントを追加
        collection.add(
            hashMapOf("first_letter" to _firstHiragana.value)
        ).addOnSuccessListener { document ->
            // 追加したドキュメントのIDを取得
            _currentGameId.value = document.id
        }
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

    fun enterAnswer(value: String) {
        _answer.value = value
    }

    fun addNewDrawingToDB() {
        val newWordChainDrawing = WordChainDrawing(
            answer = _answer.value,
            drawing = _drawingList.value
        )

        val newList = _allDrawingList.value.toMutableList()
        newList.add(newWordChainDrawing)

        _answer.value = ""
        _allDrawingList.value = newList
        _drawingList.value = emptyList()

        addToDatabase(newWordChainDrawing)
        _isDialogShow.value = !_isDialogShow.value
    }

    fun toggleIsDialogShow() {
        _isDialogShow.value = !_isDialogShow.value
        _answer.value = ""
    }

    fun addToDatabase(wordChainDrawing: WordChainDrawing) {
        val collection = database.collection("word_chain_drawing")
        // 現在のゲームIDのドキュメントを取得
        val document = collection.document(_currentGameId.value)
        // 絵を保存しているサブコレクションを取得
        val subCollection = document.collection("drawings")
        val drawingStringList = wordChainDrawing.drawing.map { path ->
            PathConverter.pathToSvg(path.asAndroidPath())
        }
        val data = hashMapOf(
            "user_id" to "user",
            "drawing" to drawingStringList,
            "answer" to wordChainDrawing.answer
        )
        subCollection.add(data)
            .addOnSuccessListener {} // 成功時の処理
            .addOnFailureListener {} // 失敗時の処理
    }
}